package pl.jozefniemiec.langninja.ui.main.languages;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesFragmentContract {

    interface View {

        void showLanguages(List<Language> languageList);

        void showLanguageDetails(String languageCode);

        void showError(String message);
    }

    interface Presenter {

        void loadLanguages();

        void onLanguageItemClicked(int position);

        void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

        int getLanguageItemsCount();
    }
}
