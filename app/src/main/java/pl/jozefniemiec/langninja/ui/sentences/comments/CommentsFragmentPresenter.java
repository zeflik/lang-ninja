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

    public static final String TAG = CommentsFragmentPresenter.class.getSimpleName();
    private final CommentsFragmentContract.View view;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private String[] menuOptions = {"Edytuj", "Usuń"};
    private String[] inappropriateContentOptions = {"Propagowanie nienawiści", "Niecenzuralne wyrazy", "Inne"};

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
                    .subscribe(view::showLoggedUserPhoto);
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
                    .doOnSubscribe(disposable -> view.showProgress())
                    .map(user -> new Author(user.getUid(), user.getName(), user.getPhoto()))
                    .map(author -> new Comment(sentenceId, commentText, author))
                    .flatMap(commentsRepository::insert)
                    .flatMap(commentsRepository::getComment)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(view::hideProgress)
                    .subscribe(
                            comment -> {
                                view.clearInputField();
                                view.hideKeyboard();
                                view.addComment(comment);
                            },
                            error -> view.showNeedInternetInfo()
                    );
        } else {
            view.showErrorMessage("Niewłaściwy format.");
        }
    }

    @Override
    public void onVoteUpButtonClicked(CommentsItemView holder, Comment comment) {
        commentsRepository
                .like(comment, authService.getCurrentUserUid())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> holder.showVoteUpProgress())
                .doFinally(holder::hideVoteUpProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newComment -> view.replaceComment(newComment, comment));
    }

    @Override
    public void onVoteDownButtonClicked(CommentsItemView holder, Comment comment) {
        commentsRepository
                .dislike(comment, authService.getCurrentUserUid())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> holder.showVoteDownProgress())
                .doFinally(holder::hideVoteDownProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newComment -> view.replaceComment(newComment, comment));
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

    @Override
    public void onCommentClicked(Comment comment) {
        String sentenceAuthorUid = comment.getAuthor().getUid();
        if (authService.isSignedIn() && authService.getCurrentUserUid().equals(sentenceAuthorUid)) {
            view.showSentenceOptionsDialog(menuOptions, comment);
        } else {
            view.showInappropriateContentDialog(inappropriateContentOptions, comment);
        }
    }

    @Override
    public void onCommentOptionSelected(int optionIndex, Comment comment) {
        switch (optionIndex) {
            case 0:
                view.editComment(comment);
                break;
            case 1:
                view.showRemoveCommentAlert(comment);
                break;
        }
    }

    @Override
    public void onInappropriateContentSelected(int item, Comment comment) {

    }

    @Override
    public void onRemoveButtonClicked(Comment comment) {
        commentsRepository
                .remove(comment)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(x -> view.showProgress())
                .doFinally(view::hideProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.removeComment(comment));
    }

    @Override
    public void onCancelButtonClicked(int position) {
        view.collapseCommentEdit(position);
    }
}
