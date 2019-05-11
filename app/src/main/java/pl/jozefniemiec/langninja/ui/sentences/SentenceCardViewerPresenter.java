package pl.jozefniemiec.langninja.ui.sentences;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.di.sentences.SentenceCardViewerActivityScope;
import pl.jozefniemiec.langninja.service.FirebaseAuthService;
import pl.jozefniemiec.langninja.utils.Utility;

@SentenceCardViewerActivityScope
public class SentenceCardViewerPresenter implements SentenceCardViewerContract.Presenter {

    private final SentenceCardViewerContract.View view;
    private final UserSentenceRepository userSentenceRepository;
    private final FirebaseAuthService authService;

    @Inject
    SentenceCardViewerPresenter(SentenceCardViewerContract.View view,
                                UserSentenceRepository userSentenceRepository,
                                FirebaseAuthService authService) {
        this.view = view;
        this.userSentenceRepository = userSentenceRepository;
        this.authService = authService;
    }

    @Override
    public void onMenuCreated(String sentenceId) {
        if (sentenceId != null && authService.isSignedIn()) {
            userSentenceRepository
                    .getSentenceOnce(sentenceId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(userSentence -> userSentence.getAuthor().getUid())
                    .map(uid -> authService.getCurrentUserUid().equals(uid))
                    .subscribe(isUidMatch -> {
                        if (isUidMatch) {
                            view.showEditButtons();
                        } else {
                            view.hideEditButtons();
                        }
                    });
        } else {
            view.hideEditButtons();
        }
    }

    @Override
    public void onTextSpokenResult(List<String> spokenTextsList) {
        String spokenTextFirstMatch = spokenTextsList.get(0);
        if (spokenTextFirstMatch.equalsIgnoreCase(Utility.removePunctuationMarks(view.getCurrentSentence()))) {
            view.showCorrectSpokenText(spokenTextFirstMatch);
        } else {
            view.showWrongSpokenText(spokenTextFirstMatch);
        }
    }

    @Override
    public void onSentencePageChanged(int position, int pageCount) {
        view.hideSpokenText();
        view.stopReading();
        view.stopListening();
        if (pageCount > 1) {
            view.updateNumbering(position, pageCount);
        } else {
            view.hideNumbering();
        }
    }

    @Override
    public void onSentenceEditButtonClicked(String sentenceId) {
        view.editSentence(sentenceId);
    }

    @Override
    public void onSentenceRemoveButtonClicked() {
        view.showRemoveSentenceAlert();
    }

    @Override
    public void removeSentence(String sentenceId) {
        userSentenceRepository
                .remove(sentenceId)
                .subscribe(() -> {
                               view.notifyDataChanged();
                               view.close();
                           },
                           error -> view.showErrorMessage(error.getMessage())
                );
    }

    @Override
    public void onCommentsButtonPressed() {
        view.showComments();
    }
}
