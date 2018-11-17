package pl.jozefniemiec.langninja.ui.main.languages;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;

public class LanguagesFragmentPresenter implements LanguagesFragmentContract.Presenter {

    private final LanguagesFragmentContract.View view;
    private final LanguageRepository languageRepository;

    @Inject
    LanguagesFragmentPresenter(LanguagesFragmentContract.View view,
                               LanguageRepository languageRepository) {
        this.view = view;
        this.languageRepository = languageRepository;
    }

    @Override
    public void onViewCreated() {
        try {
            view.showLanguages(languageRepository.getAll());
        } catch (RuntimeException e) {
            view.showErrorMessage("Błąd bazy danych!");
        }
    }

    @Override
    public void onLanguageItemClicked(Language language) {
        view.showLanguageDetails(language.getCode());
    }
}






