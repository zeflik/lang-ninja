package pl.jozefniemiec.langninja.ui.main.fragment.home.view;

import java.util.List;

import pl.jozefniemiec.langninja.model.Language;

public interface HomeFragmentView {

    void showLanguages(List<Language> languageList);

    void showLanguageDetails(String languageCode);

    void showError(String message);
}