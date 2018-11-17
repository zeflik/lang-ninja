package pl.jozefniemiec.langninja.ui.main.languages;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesFragmentContract {

    interface View {

        void showLanguages(List<Language> languageList);

        void showLanguageDetails(String languageCode);

        void showErrorMessage(String message);
    }

    interface Presenter {

        void onViewCreated();

        void onLanguageItemClicked(Language language);
    }
}
