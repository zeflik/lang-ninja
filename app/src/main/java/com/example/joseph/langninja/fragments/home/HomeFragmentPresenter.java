package com.example.joseph.langninja.fragments.home;

import com.example.joseph.langninja.fragments.home.components.LanguageItemView;
import com.example.joseph.langninja.fragments.home.resources.ResourcesManager;
import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.repository.LanguageRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentPresenter {

    private final HomeFragmentView view;
    private final List<Language> languages = new ArrayList<>();
    private final ResourcesManager resourcesManager;
    private final LanguageRepository languageRepository;


    public HomeFragmentPresenter(HomeFragmentView view, LanguageRepository languageRepository, ResourcesManager resourcesManager) {
        this.view = view;
        this.resourcesManager = resourcesManager;
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

    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView) {
        itemView.setLanguageNativeName(languages.get(position).getNativeName());
        itemView.setLanguageFlag(resourcesManager.getFlagId(languages.get(position).getCode().toLowerCase()));
        itemView.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
    }

    public int getLanguageItemsCount() {
        return languages.size();
    }

    public void onItemClicked(int position) {
        view.showLanguageDetails(languages.get(position).getCode());
    }
}





