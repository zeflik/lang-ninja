package pl.jozefniemiec.langninja.ui.main.community;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface CommunityFragmentContract {

    interface View {

        void addData(UserSentence userSentence);

        void clearData();
    }

    interface Presenter {

        void onOptionSelected(Language language, int option);

        void onDestroyView();
    }

}
