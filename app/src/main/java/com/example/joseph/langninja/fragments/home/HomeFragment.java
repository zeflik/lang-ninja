package com.example.joseph.langninja.fragments.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joseph.langninja.R;
import com.example.joseph.langninja.dao.AppDatabase;
import com.example.joseph.langninja.dao.LanguageDao;
import com.example.joseph.langninja.fragments.home.components.LanguagesViewAdapter;
import com.example.joseph.langninja.fragments.home.resources.AndroidResourceManager;
import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.utils.Utility;

import java.util.List;

public class HomeFragment extends Fragment implements HomeFragmentView {

    private static final String TAG = "HomeFragment";

    LanguagesViewAdapter adapter;
    RecyclerView recyclerView;
    HomeFragmentPresenter homeFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.rvNumbers);
        int numberOfColumns = Utility.calculateNoOfColumns(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        LanguageDao languageDao = AppDatabase.getInstance(getContext()).languageDao();
        AndroidResourceManager androidResourceManager = new AndroidResourceManager(getResources());
        homeFragmentPresenter = new HomeFragmentPresenter(this, languageDao, androidResourceManager);
        homeFragmentPresenter.loadLanguages();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        adapter = new LanguagesViewAdapter(homeFragmentPresenter, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openLanguagePage(String languageCode) {
        Toast.makeText(getContext(), "creating activity " + languageCode, Toast.LENGTH_LONG).show();
    }
}
