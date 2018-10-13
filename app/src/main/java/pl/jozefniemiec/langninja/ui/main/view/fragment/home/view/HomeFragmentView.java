package pl.jozefniemiec.langninja.ui.main.view.fragment.home.view;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface HomeFragmentView {

    void showLanguages(List<Language> languageList);

    void showLanguageDetails(String languageCode);

    void showError(String message);
}
