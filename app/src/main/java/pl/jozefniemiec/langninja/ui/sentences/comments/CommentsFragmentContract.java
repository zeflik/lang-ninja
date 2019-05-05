package pl.jozefniemiec.langninja.ui.sentences.comments;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;

public interface CommentsFragmentContract {

    interface View {

        void close();

        void showProgress();

        void hideProgress();

        void notifyDataChanged();

        void showNeedInternetInfo();

        void showErrorMessage(String message);

        void showData(List<Comment> comments);

        void clearInputField();

        void hideKeyboard();

        void showNewItem(Comment comment);

        void showInputPanel();

        void hideInputPanel();

        void showUserPhoto(String photo);

        void showSentenceOptionsDialog(String[] menuOptions, Comment comment);

        void showInappropriateContentDialog(String[] inappropriateContentOptions, Comment comment);

        void showRemoveCommentAlert(Comment comment);

        void removeComment(Comment comment);

        void replaceComment(Comment newComment, Comment comment);
    }

    interface Presenter {

        void onViewCreated(String sentenceId);

        void onCreateCommentClicked(String sentenceId, String commentText);

        void onVoteUpButtonClicked(CommentsItemView holder, Comment comment);

        void onVoteDownButtonClicked(CommentsItemView holder, Comment comment);

        void onItemViewLikesBind(CommentsItemView itemView, Likes likes);

        void onItemViewReplayButtonPressed(CommentsItemView itemView);

        void onItemLongButtonClicked(Comment comment);

        void onCommentOptionSelected(int item, Comment comment);

        void onInappropriateContentSelected(int item, Comment comment);

        void onRemoveButtonClicked(Comment comment);
    }
}
