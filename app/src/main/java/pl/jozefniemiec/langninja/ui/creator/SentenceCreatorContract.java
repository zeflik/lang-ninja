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
    }

    interface Presenter {

        void onViewCreated();

        void createButtonClicked(String langCode, String sentence);

        void onImageButtonClicked();

        void onLanguagePicked(Language language);
    }
}
