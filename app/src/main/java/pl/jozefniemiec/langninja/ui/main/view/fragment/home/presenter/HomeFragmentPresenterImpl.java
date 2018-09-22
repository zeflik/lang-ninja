package pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragmentView;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.adapter.LanguageItemView;

public class HomeFragmentPresenterImpl implements HomeFragmentPresenter {

    private final HomeFragmentView view;
    private final LanguageRepository languageRepository;
    private List<Language> languages = new ArrayList<>();
    private final ResourcesManager resourcesManager;


    @Inject
    public HomeFragmentPresenterImpl(HomeFragmentView view, LanguageRepository languageRepository, ResourcesManager resourcesManager) {
        this.view = view;
        this.languageRepository = languageRepository;
        this.resourcesManager = resourcesManager;
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

    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView) {
        itemView.setLanguageNativeName(languages.get(position).getNativeName());
        itemView.setLanguageFlag(resourcesManager.getFlagId(languages.get(position).getCode().toLowerCase()));
        itemView.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
    }

    public int getLanguageItemsCount() {
        return languages.size();
    }
}






