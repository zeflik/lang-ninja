package pl.jozefniemiec.langninja.ui.creator.languageslist;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.di.creator.languageslist.LanguagesListFragmentScope;

@LanguagesListFragmentScope
public class LanguagesListPresenter implements LanguagesListContract.Presenter {

    private final LanguagesListContract.View view;
    private final LanguageRepository languageRepository;

    @Inject
    LanguagesListPresenter(LanguagesListContract.View view, LanguageRepository languageRepository) {
        this.view = view;
        this.languageRepository = languageRepository;
    }

    public void onViewCreated() {
        view.showLanguages(languageRepository.getAll());
    }
}
