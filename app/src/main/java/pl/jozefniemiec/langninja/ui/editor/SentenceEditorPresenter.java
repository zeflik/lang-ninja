package pl.jozefniemiec.langninja.ui.editor;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.editor.SentenceEditorScope;
import pl.jozefniemiec.langninja.utils.Utility;

@SentenceEditorScope
public class SentenceEditorPresenter implements SentenceEditorContract.Presenter {

    private final String TAG = SentenceEditorPresenter.class.getSimpleName();
    private final SentenceEditorContract.View view;
    private final UserSentenceRepository userSentenceRepository;
    private final ResourcesManager resourcesManager;
    private UserSentence currentUserSentence;

    @Inject
    public SentenceEditorPresenter(SentenceEditorContract.View view,
                                   UserSentenceRepository userSentenceRepository,
                                   ResourcesManager resourcesManager) {
        this.view = view;
        this.userSentenceRepository = userSentenceRepository;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void loadData(String sentenceId) {
        userSentenceRepository
                .getSentenceOnce(sentenceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userSentence -> {
                            currentUserSentence = userSentence;
                            view.showSentence(userSentence.getSentence());
                            view.showFlag(resourcesManager.getFlagId(userSentence.getLanguageCode()));
                        },
                        error -> {
                        }
                );
    }

    @Override
    public void onSaveButtonClicked(String sentence) {
        if (currentUserSentence.getSentence().equals(sentence)) {
            view.close();
        } else if (Utility.validateSentenceText(sentence)) {
            currentUserSentence.setSentence(sentence);
            userSentenceRepository
                    .update(currentUserSentence)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .doOnComplete(view::notifyDataChanged)
                    .subscribe(
                            () -> view.closeAndReturnResult(sentence),
                            error -> view.showNeedInternetInfo()
                    );
        } else {
            view.showErrorMessage("Wprowadź tekst!");
        }
    }
}