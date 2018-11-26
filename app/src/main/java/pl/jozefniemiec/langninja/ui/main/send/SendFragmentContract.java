package pl.jozefniemiec.langninja.ui.main.send;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface SendFragmentContract {

    interface View {

        void showData(List<UserSentence> userSentences);

        void addData(UserSentence userSentence);
    }

    interface Presenter {

        void onViewVisible();

        void onViewInvisible();

        void loadData(String userUid);
    }

}
