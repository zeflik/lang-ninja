package com.example.joseph.langninja.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joseph.langninja.R;
import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    HomeRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = getView().findViewById(R.id.rvNumbers);

        int numberOfColumns = Utility.calculateNoOfColumns(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        String[] languageNames = getResources().getStringArray(R.array.language_names);
        String[] languageNativeNames = getResources().getStringArray(R.array.language_native_names);
        adapter = new HomeRecyclerViewAdapter(recyclerView, languageNames, languageNativeNames);
        adapter.setData(createLanguageCodes());
        recyclerView.setAdapter(adapter);
    }

    private List<Language> createLanguageCodes() {
        List<String> langCodes = Arrays.asList(getResources().getStringArray(R.array.language_codes));
        List<Language> languages = new ArrayList<>();
        for (String lancode : langCodes) {
            languages.add(new Language(lancode));
        }
        return languages;
    }
}
