package pl.jozefniemiec.langninja.ui.sentences.community;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.NoInternetConnectionException;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.sentences.community.CommunityCardScope;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

@CommunityCardScope
public class CommunityCardPresenter implements CommunityCardContract.Presenter {

    private final String TAG = CommunityCardPresenter.class.getSimpleName();
    private final CommunityCardContract.View view;
    private final UserSentenceRepository userSentenceRepository;
    private final AuthService authService;
    private final InternetConnectionService internetConnectionService;
    private final ResourcesManager resourcesManager;
    private Disposable sentencesSubscription;
    private Disposable likesSubscription;
    private Disposable likeSubscription;
    private Disposable dislikeSubscription;

    @Inject
    CommunityCardPresenter(CommunityCardContract.View view,
                           UserSentenceRepository userSentenceRepository,
                           AuthService authService,
                           InternetConnectionService internetConnectionService, ResourcesManager resourcesManager) {
        this.view = view;
        this.userSentenceRepository = userSentenceRepository;
        this.authService = authService;
        this.internetConnectionService = internetConnectionService;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void loadData(String sentenceKey) {
        sentencesSubscription = userSentenceRepository
                .getSentence(sentenceKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showData);

        likesSubscription = userSentenceRepository
                .getLikes(sentenceKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(likes -> {
                    view.showLikesCount(String.valueOf(likes.getCount()));
                    if (authService.isSignedIn()) {
                        String userUid = authService.getCurrentUserUid();
                        if (likes.getLikesMap().containsKey(userUid)
                                && likes.getLikesMap().get(userUid)) {
                            view.unHighlightDislikeButton();
                            view.highlightLikeButton();
                        } else if (likes.getDislikesMap().containsKey(userUid)
                                && likes.getDislikesMap().get(userUid)) {
                            view.unHighlightLikeButton();
                            view.highlightDislikeButton();
                        } else {
                            view.unHighlightLikeButton();
                            view.unHighlightDislikeButton();
                        }
                    }
                });
    }

    @Override
    public void onLikeButtonClicked(UserSentence userSentence) {
        if (authService.isSignedIn()) {
            String sentenceId = userSentence.getId();
            String languageCode = userSentence.getLanguageCode();
            String userUid = authService.getCurrentUserUid();
            likeSubscription = userSentenceRepository
                    .like(sentenceId, languageCode, userUid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(completable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .subscribe(view::notifyDataChanged, this::onError);
        } else {
            view.showSignInDialog();
        }
    }

    @Override
    public void onDislikeButtonClicked(UserSentence userSentence) {
        if (authService.isSignedIn()) {
            String sentenceId = userSentence.getId();
            String languageCode = userSentence.getLanguageCode();
            String userUid = authService.getCurrentUserUid();
            dislikeSubscription = userSentenceRepository
                    .dislike(sentenceId, languageCode, userUid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(completable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .subscribe(view::notifyDataChanged, this::onError);
        } else {
            view.showSignInDialog();
        }
    }

    @Override
    public void onViewClose() {
        userSentenceRepository.dispose();
        if (sentencesSubscription != null && !sentencesSubscription.isDisposed()) {
            sentencesSubscription.dispose();
        }
        if (likesSubscription != null && !likesSubscription.isDisposed()) {
            likesSubscription.dispose();
        }
        if (likeSubscription != null && !likeSubscription.isDisposed()) {
            likeSubscription.dispose();
        }
        if (dislikeSubscription != null && !dislikeSubscription.isDisposed()) {
            dislikeSubscription.dispose();
        }
    }

    private void onError(Throwable throwable) {
        if (throwable instanceof NoInternetConnectionException) {
            view.showNeedInternetInfo();
        } else {
            Log.d(TAG, "onError: " + throwable.getMessage());
        }
    }
}
