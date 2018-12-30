package pl.jozefniemiec.langninja.ui.sentences.community;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
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
    private Disposable subscription;

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
        subscription = userSentenceRepository
                .getSentence(sentenceKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userSentence -> {
                    view.showData(userSentence);
                    if (authService.isSignedIn()) {
                        String userUid = authService.getCurrentUserUid();
                        if (userSentence.getLikes().getLikesMap().containsKey(userUid)) {
                            view.unHighlightDislikeButton();
                            view.highlightLikeButton();
                        } else if (userSentence.getLikes().getDislikesMap().containsKey(userUid)) {
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
    public void onLikeButtonClicked(String sentenceKey) {
        if (authService.isSignedIn()) {
            if (internetConnectionService.isInternetOn()) {
                userSentenceRepository
                        .like(sentenceKey, authService.getCurrentUserUid())
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
    public void onDislikeButtonClicked(String sentenceKey) {
        if (authService.isSignedIn()) {
            if (internetConnectionService.isInternetOn()) {
                userSentenceRepository
                        .dislike(sentenceKey, authService.getCurrentUserUid())
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
        subscription.dispose();
    }
}
