package pl.jozefniemiec.langninja.ui.main.send.creator;

import android.util.Log;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.SentenceCandidateRepository;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.ui.main.send.creator.di.SentenceCreatorScope;

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
        SentenceCandidate sentenceCandidate = new SentenceCandidate(langCode, sentence);
        String currentUserUid = authService.getCurrentUserUid();
        sentenceCandidateRepository.insertByUserUid(currentUserUid, sentenceCandidate);
        view.close();
    }
}
