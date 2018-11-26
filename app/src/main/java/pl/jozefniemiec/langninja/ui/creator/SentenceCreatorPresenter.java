package pl.jozefniemiec.langninja.ui.creator;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.service.AuthService;

@SentenceCreatorScope
public class SentenceCreatorPresenter implements SentenceCreatorContract.Presenter {

    private final static String TAG = SentenceCreatorPresenter.class.getSimpleName();
    private final static String AUTH_ERROR_MESSAGE = "Creating sentence candidates only for authenticated users.";
    private final SentenceCreatorContract.View view;
    private final AuthService authService;
    private final UserSentenceRepository sentenceCandidateRepository;

    @Inject
    SentenceCreatorPresenter(SentenceCreatorContract.View view,
                             AuthService authService,
                             UserSentenceRepository sentenceCandidateRepository) {
        this.view = view;
        this.authService = authService;
        this.sentenceCandidateRepository = sentenceCandidateRepository;
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onCreateButtonClicked(String langCode, String sentence) {
        if (langCode == null) {
            view.showErrorMessage("Wybierz język!");
            return;
        }
        if (sentence.trim().isEmpty()) {
            view.showErrorMessage("Wprowadź tekst!");
            return;
        }
        String currentUserUid = authService.getCurrentUserUid();
        UserSentence sentenceCandidate = new UserSentence(sentence, langCode, currentUserUid);
        sentenceCandidateRepository.insert(sentenceCandidate);
        view.close();
    }

    @Override
    public void onFlagImageButtonClicked() {
        view.hideKeyboard();
        view.showLanguagesListWindow();
    }

    @Override
    public void onLanguagePicked(Language language) {
        view.hideLanguagesListWindow();
        view.showLanguageData(language);
        view.showKeyboard();
    }

    @Override
    public void onTestButtonClicked(String langCode, String sentence) {
        if (langCode == null) {
            view.showErrorMessage("Wybierz język!");
            return;
        }
        if (sentence.trim().isEmpty()) {
            view.showErrorMessage("Wprowadź tekst!");
            return;
        }
        view.showSentenceCard(langCode, sentence);
    }
}
