package pl.jozefniemiec.langninja.ui.creator;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface SentenceCreatorContract {

    interface View {

        void showKeyboard();

        void hideKeyboard();

        void showErrorMessage(String message);

        void showSentenceCard(String langCode, String sentence);

        void initializeSpinner(List<Language> languages);

        void close();
    }

    interface Presenter {

        void onViewCreated();

        void onCreateButtonClicked(Language language, String sentence);

        void onTestButtonClicked(Language language, String sentence);
    }
}
