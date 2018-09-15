package com.example.joseph.langninja.fragments.home;

import com.example.joseph.langninja.model.Language;

import java.util.List;

public interface HomeFragmentView {

    void showLanguages(List<Language> languageList);

    void openLanguagePage(String languageCode);
}
