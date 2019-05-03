package pl.jozefniemiec.langninja.ui.sentences.comments;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.CommentsRepository;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Author;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;
import pl.jozefniemiec.langninja.di.sentences.comments.CommentsFragmentScope;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.utils.Utility;

@CommentsFragmentScope
public class CommentsFragmentPresenter implements CommentsFragmentContract.Presenter {

    private final CommentsFragmentContract.View view;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Inject
    CommentsFragmentPresenter(CommentsFragmentContract.View view,
                              CommentsRepository commentsRepository,
                              UserRepository userRepository,
                              AuthService authService) {
        this.view = view;
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public void onViewCreated(String sentenceId) {
        if (authService.isSignedIn()) {
            view.showInputPanel();
            userRepository
                    .getUser(authService.getCurrentUserUid())
                    .subscribeOn(Schedulers.io())
                    .map(User::getPhoto)
                    .doOnSubscribe(disposable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(view::showUserPhoto);
        } else {
            view.hideInputPanel();
        }
        commentsRepository
                .getCommentsBySentenceId(sentenceId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> view.showProgress())
                .doFinally(view::hideProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showData,
                        error -> view.showNeedInternetInfo()
                );
    }

    @Override
    public void onCreateCommentClicked(String sentenceId, String commentText) {
        if (Utility.validateCommentText(commentText)) {
            userRepository
                    .getUser(authService.getCurrentUserUid())
                    .subscribeOn(Schedulers.io())
                    .map(user -> new Author(user.getUid(), user.getName(), user.getPhoto()))
                    .map(author -> new Comment(sentenceId, commentText, author))
                    .doOnSubscribe(disposable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            comment -> {
                                commentsRepository
                                        .insert(comment)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSubscribe(disposable -> view.showProgress())
                                        .doFinally(view::hideProgress)
                                        .subscribe(
                                                id -> {
                                                    view.notifyDataChanged();
                                                    view.clearInputField();
                                                    view.hideKeyboard();
                                                    view.showNewItem(comment);
                                                },
                                                error -> view.showNeedInternetInfo()
                                        );
                            }//TODO - refactor
                            , error -> {
                                view.showNeedInternetInfo();
                            }
                    );
        } else {
            view.showErrorMessage("Niewłaściwy format.");
        }
    }

    @Override
    public void onVoteUpButtonClicked(CommentsItemView holder, Comment comment) {
        commentsRepository
                .like(comment.getSentenceId(), comment.getId(), authService.getCurrentUserUid())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> holder.showVoteUpProgress())
                .doOnComplete(() -> changeButtonStatesOnVoteUp(holder))
                .doOnComplete(() -> changeLikesCountColor(holder))
                .doFinally(holder::hideVoteUpProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void changeButtonStatesOnVoteUp(CommentsItemView holder) {
        holder.changeLikesCountByValue(holder.isVoteUpButtonSelected() ? -1 : 1);
        holder.selectVoteUpButton(!holder.isVoteUpButtonSelected());
        holder.changeLikesCountByValue(holder.isVoteDownButtonSelected() ? 1 : 0);
        holder.selectVoteDownButton(false);
    }

    @Override
    public void onVoteDownButtonClicked(CommentsItemView holder, Comment comment) {
        commentsRepository
                .dislike(comment.getSentenceId(), comment.getId(), authService.getCurrentUserUid())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> holder.showVoteDownProgress())
                .doOnComplete(() -> changeButtonStatesOnVoteDown(holder))
                .doOnComplete(() -> changeLikesCountColor(holder))
                .doFinally(holder::hideVoteDownProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void changeButtonStatesOnVoteDown(CommentsItemView holder) {
        holder.changeLikesCountByValue(holder.isVoteDownButtonSelected() ? 1 : -1);
        holder.selectVoteDownButton(!holder.isVoteDownButtonSelected());
        holder.changeLikesCountByValue(holder.isVoteUpButtonSelected() ? -1 : 0);
        holder.selectVoteUpButton(false);
    }

    private void changeLikesCountColor(CommentsItemView holder) {
        if (holder.getLikesCount() < 0) {
            holder.indicateNegativeNumber();
        } else {
            holder.indicatePositiveNumber();
        }
    }

    @Override
    public void onItemViewLikesBind(CommentsItemView itemView, Likes likes) {
        if (authService.isSignedIn() && likes != null) {
            if (likes.getCount() < 0) {
                itemView.indicateNegativeNumber();
            }
            String currentUserUid = authService.getCurrentUserUid();
            if (likes.getLikesMap() != null && likes.getLikesMap().containsKey(currentUserUid)) {
                itemView.selectVoteUpButton(likes.getLikesMap().get(currentUserUid));
            }
            if (likes.getDislikesMap() != null && likes.getDislikesMap().containsKey(currentUserUid)) {
                itemView.selectVoteDownButton(likes.getDislikesMap().get(currentUserUid));
            }
        }
    }

    @Override
    public void onItemViewReplayButtonPressed(CommentsItemView itemView) {
        itemView.showReplaysList();
    }
}
