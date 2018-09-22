package pl.jozefniemiec.langninja.ui.main.fragment.home.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragmentView;

public class HomeFragmentPresenterImpl implements HomeFragmentPresenter {

    private final HomeFragmentView view;
    private final LanguageRepository languageRepository;
    private List<Language> languages = new ArrayList<>();

    @Inject
    public HomeFragmentPresenterImpl(HomeFragmentView view, LanguageRepository languageRepository) {
        this.view = view;
        this.languageRepository = languageRepository;
    }

    public void loadLanguages() {
        try {
            languages.addAll(languageRepository.getAll());
            view.showLanguages(languages);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    public void onLanguageItemClicked(int position) {
        view.showLanguageDetails(languages.get(position).getCode());
    }
}






