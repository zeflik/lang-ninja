package pl.jozefniemiec.langninja.ui.creator;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface SentenceCreatorContract {

    interface View {

        void close();

        void showKeyboard();

        void hideKeyboard();

        void startLanguagesListForResult();

        void showErrorMessage(String message);

        void showLanguagesListWindow();

        void hideLanguagesListWindow();

        void showLanguageData(Language language);

        void showSentenceCard(String langCode, String sentence);
    }

    interface Presenter {

        void onViewCreated();

        void onCreateButtonClicked(String langCode, String sentence);

        void onFlagImageButtonClicked();

        void onLanguagePicked(Language language);

        void onTestButtonClicked(String langCode, String sentence);
    }
}
