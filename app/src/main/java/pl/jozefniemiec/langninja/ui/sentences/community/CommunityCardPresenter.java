package pl.jozefniemiec.langninja.ui.sentences.community;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
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
    private Disposable sentencesSubscription;
    private Disposable likesSubscription;

    @Inject
    CommunityCardPresenter(CommunityCardContract.View view,
                           UserSentenceRepository userSentenceRepository,
                           AuthService authService,
                           InternetConnectionService internetConnectionService) {
        this.view = view;
        this.userSentenceRepository = userSentenceRepository;
        this.authService = authService;
        this.internetConnectionService = internetConnectionService;
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
            if (internetConnectionService.isInternetOn()) {
                String sentenceId = userSentence.getId();
                String languageCode = userSentence.getLanguageCode();
                String userUid = authService.getCurrentUserUid();
                userSentenceRepository
                        .like(sentenceId, languageCode, userUid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            } else {
                view.showNeedInternetDialog();
            }
        } else {
            view.showSignInDialog();
        }
    }

    @Override
    public void onDislikeButtonClicked(UserSentence userSentence) {
        if (authService.isSignedIn()) {
            if (internetConnectionService.isInternetOn()) {
                userSentenceRepository
                        .dislike(userSentence.getId(), userSentence.getLanguageCode(), authService.getCurrentUserUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            } else {
                view.showNeedInternetDialog();
            }
        } else {
            view.showSignInDialog();
        }
    }

    @Override
    public void onViewClose() {
        sentencesSubscription.dispose();
        likesSubscription.dispose();
    }
}
