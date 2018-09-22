package pl.jozefniemiec.langninja.ui.main;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;

public class MainPresenterImpl implements MainPresenter {

    private final MainView view;
    private final LanguageRepository languageRepository;

    @Inject
    MainPresenterImpl(MainView view, LanguageRepository languageRepository) {
        this.view = view;
        this.languageRepository = languageRepository;
    }

    private void initializeDatabase() {
        languageRepository.insertAll(
                new Language("pl_PL", "Polski"),
                new Language("en_GB", "English"),
                new Language("de", "German")
        );
    }

    @Override
    public void loadMain() {
        initializeDatabase();
        view.showFragments();
    }

    @Override
    public void onExitCleanup() {
        languageRepository.destroyInstance();
    }
}
