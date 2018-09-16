package pl.jozefniemiec.langninja.activities.main.fragments.home.presenter;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.activities.main.fragments.home.view.HomeFragmentView;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;

public class HomeFragmentPresenterImpl implements HomeFragmentPresenter {

    private final HomeFragmentView view;
    private final LanguageRepository languageRepository;
    private List<Language> languages = new ArrayList<>();


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






