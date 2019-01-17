package pl.jozefniemiec.langninja.ui.main.community;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface CommunityFragmentContract {

    interface View {

        void addData(List<UserSentence> userSentences);

        void showSentenceDetails(UserSentence userSentence);

        void openNewSentencePage(String languageCode);

        void clearData();

        void showSentenceOptionsDialog(CharSequence[] options, UserSentence userSentence);

        void showInappropriateContentDialog(CharSequence[] reasons, UserSentence userSentence);

        void removeItem(UserSentence userSentence);

        void registerOnDataChangeListener();

        void unregisterOnDataChangeListener();
    }

    interface Presenter {

        void onViewCreated();

        void pullData(Language language, int option);

        void onDestroyView();

        void onShowButtonClicked(UserSentence userSentence);

        void onCreateSentenceButtonClicked(Language selectedItem);

        void onItemLongButtonClicked(UserSentence userSentence);

        void onInappropriateContentSelected(int reasonIndex, UserSentence userSentence);

        void onSentenceOptionSelected(int optionIndex, UserSentence userSentence);
    }
}
