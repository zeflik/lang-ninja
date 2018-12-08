package pl.jozefniemiec.langninja.ui.creator;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.service.AuthService;

@SentenceCreatorScope
public class SentenceCreatorPresenter implements SentenceCreatorContract.Presenter {

    private final static String TAG = SentenceCreatorPresenter.class.getSimpleName();
    private final SentenceCreatorContract.View view;
    private final AuthService authService;
    private final UserSentenceRepository sentenceCandidateRepository;
    private final LanguageRepository languageRepository;

    @Inject
    SentenceCreatorPresenter(SentenceCreatorContract.View view,
                             AuthService authService,
                             UserSentenceRepository sentenceCandidateRepository, LanguageRepository languageRepository) {
        this.view = view;
        this.authService = authService;
        this.sentenceCandidateRepository = sentenceCandidateRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public void onViewCreated() {
        view.initializeSpinner(languageRepository.getAll());
    }

    @Override
    public void onCreateButtonClicked(Language language, String sentence) {
        if (language == null) {
            view.showErrorMessage("Wybierz język!");
            return;
        }
        if (sentence.trim().isEmpty()) {
            view.showErrorMessage("Wprowadź tekst!");
            return;
        }
        String currentUserUid = authService.getCurrentUserUid();
        UserSentence sentenceCandidate = new UserSentence(sentence, language.getCode(), currentUserUid);
        sentenceCandidateRepository.insert(sentenceCandidate);
        view.close();
    }

    @Override
    public void onTestButtonClicked(Language language, String sentence) {
        if (language == null) {
            view.showErrorMessage("Wybierz język!");
            return;
        }
        if (sentence.trim().isEmpty()) {
            view.showErrorMessage("Wprowadź tekst!");
            return;
        }
        view.showSentenceCard(language.getCode(), sentence);
    }
}
