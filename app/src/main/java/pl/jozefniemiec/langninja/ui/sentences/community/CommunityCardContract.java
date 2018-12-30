package pl.jozefniemiec.langninja.ui.sentences.community;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface CommunityCardContract {

    interface View {

        void showData(UserSentence userSentence);

        void highlightLikeButton();

        void highlightDislikeButton();

        void unHighlightLikeButton();

        void unHighlightDislikeButton();

        void showErrorMessage(String message);

        void showSignInDialog();

        void showNeedInternetDialog();
    }

    interface Presenter {

        void loadData(String sentenceKey);

        void onLikeButtonClicked(String sentenceKey);

        void onDislikeButtonClicked(String sentenceKey);

        void onViewClose();
    }
}
