package pl.jozefniemiec.langninja.ui.sentences.community;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface CommunityFeedbackContract {

    interface View {

        void showData(UserSentence userSentence);
    }

    interface Presenter {

        void loadData(String sentenceKey);
    }
}
