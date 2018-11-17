package pl.jozefniemiec.langninja.ui.creator;

import android.util.Log;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.SentenceCandidateRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.service.AuthService;

@SentenceCreatorScope
public class SentenceCreatorPresenter implements SentenceCreatorContract.Presenter {

    private final static String TAG = SentenceCreatorPresenter.class.getSimpleName();
    private final static String AUTH_ERROR_MESSAGE = "Creating sentence candidates only for authenticated users.";
    private final SentenceCreatorContract.View view;
    private final AuthService authService;
    private final SentenceCandidateRepository sentenceCandidateRepository;

    @Inject
    SentenceCreatorPresenter(SentenceCreatorContract.View view,
                             AuthService authService,
                             SentenceCandidateRepository sentenceCandidateRepository) {
        this.view = view;
        this.authService = authService;
        this.sentenceCandidateRepository = sentenceCandidateRepository;
    }

    @Override
    public void onViewCreated() {
        if (!authService.checkSignInStatus()) {
            Log.e(TAG, AUTH_ERROR_MESSAGE);
            view.close();
        }
    }

    @Override
    public void createButtonClicked(String langCode, String sentence) {
        if (langCode == null) {
            view.showErrorMessage("Wybierz język!");
            return;
        }
        if (sentence.trim().isEmpty()) {
            view.showErrorMessage("Wprowadź tekst!");
            return;
        }
        SentenceCandidate sentenceCandidate = new SentenceCandidate(sentence, langCode);
        String currentUserUid = authService.getCurrentUserUid();
        sentenceCandidateRepository.insertByUserUid(currentUserUid, sentenceCandidate);
        view.close();
    }

    @Override
    public void onImageButtonClicked() {
        view.hideKeyboard();
        view.showLanguagesListWindow();
    }

    @Override
    public void onLanguagePicked(Language language) {
        view.hideLanguagesListWindow();
        view.showLanguageData(language);
        view.showKeyboard();
    }
}
