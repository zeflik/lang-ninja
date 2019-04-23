package pl.jozefniemiec.langninja.ui.sentences.comments;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;

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
    }

    interface Presenter {

        void onViewCreated(String sentenceId);

        void onCreateCommentClicked(String sentenceId, String commentText);

        void onVoteUpButtonClicked(Comment comment);

        void onVoteDownButtonClicked(Comment comment);
    }
}
