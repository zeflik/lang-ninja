package pl.jozefniemiec.langninja.ui.main.languages;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesFragmentView {

    void showLanguages(List<Language> languageList);

    void showLanguageDetails(String languageCode);

    void showError(String message);
}
