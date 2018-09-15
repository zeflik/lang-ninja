package com.example.joseph.langninja.fragments.home;

import com.example.joseph.langninja.dao.LanguageDao;
import com.example.joseph.langninja.fragments.home.components.LanguageItemView;
import com.example.joseph.langninja.fragments.home.resources.ResourcesManager;
import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentPresenter {

    private final HomeFragmentView fragment;
    private final List<Language> languages = new ArrayList<>();
    private final ResourcesManager resourcesManager;


    public HomeFragmentPresenter(HomeFragmentView fragment, LanguageDao languageDao, ResourcesManager resourcesManager) {
        this.fragment = fragment;
        this.resourcesManager = resourcesManager;
        languages.addAll(languageDao.getAll());
    }

    public void loadLanguages() {
        fragment.showLanguages(languages);
    }

    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView) {
        itemView.setLanguageNativeName(languages.get(position).getNativeName());
        itemView.setLanguageFlag(Utility.getDrawableResId(languages.get(position).getCode().toLowerCase()));
        itemView.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
    }

    public int getLanguageItemsCount() {
        return languages.size();
    }

    public void onItemClicked(int position) {
        fragment.openLanguagePage(languages.get(position).getCode());
    }
}





