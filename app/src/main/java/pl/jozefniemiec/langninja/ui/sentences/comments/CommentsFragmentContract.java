package pl.jozefniemiec.langninja.ui.sentences.comments;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;

public interface CommentsFragmentContract {

    interface View {

        void showData(List<Comment> comments);

        void showLoggedUserPhoto(String photo);

        void showInputPanel();

        void hideInputPanel();

        void clearInputField();

        void addComment(Comment comment);

        void replaceComment(Comment newComment, Comment comment);

        void editComment(Comment comment);

        void collapseCommentEdit(int position);

        void removeComment(Comment comment);

        void notifyDataChanged();

        void showRemoveCommentAlert(Comment comment);

        void showInappropriateContentDialog(String[] inappropriateContentOptions, Comment comment);

        void showSentenceOptionsDialog(String[] menuOptions, Comment comment);

        void showErrorMessage(String message);

        void showNeedInternetInfo();

        void hideKeyboard();

        void showProgress();

        void hideProgress();

        void close();
    }
    interface Presenter {

        void onViewCreated(String sentenceId);

        void onItemViewLikesBind(CommentsItemView itemView, Likes likes);

        void onCreateCommentClicked(String sentenceId, String commentText);

        void onVoteUpButtonClicked(CommentsItemView holder, Comment comment);

        void onVoteDownButtonClicked(CommentsItemView holder, Comment comment);

        void onItemViewReplayButtonPressed(CommentsItemView itemView);

        void onCommentClicked(Comment comment);

        void onCommentOptionSelected(int item, Comment comment);

        void onInappropriateContentSelected(int item, Comment comment);

        void onRemoveButtonClicked(Comment comment);

        void onCancelButtonClicked(int holder);
    }
}
