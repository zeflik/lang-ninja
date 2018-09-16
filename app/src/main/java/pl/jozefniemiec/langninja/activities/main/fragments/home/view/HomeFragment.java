package pl.jozefniemiec.langninja.activities.main.fragments.home.view;

import android.content.Intent;
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

import java.util.List;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.language.view.LanguageCard;
import pl.jozefniemiec.langninja.activities.main.fragments.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.activities.main.fragments.home.presenter.HomeFragmentPresenterImpl;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.presenter.LanguageItemPresenter;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.presenter.LanguageItemPresenterImpl;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.view.LanguagesViewAdapter;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.utils.Utility;

public class HomeFragment extends Fragment implements HomeFragmentView, View.OnClickListener {

    private static final String TAG = "HomeFragment";
    public static final String LANGUAGE_CODE = "Language Code";

    private RecyclerView recyclerView;
    private HomeFragmentPresenter presenter;

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
        presenter = new HomeFragmentPresenterImpl(this, new RoomLanguageRepository(getContext()));
        presenter.loadLanguages();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        ResourcesManager resourcesManager = new AndroidResourceManager(getResources());
        LanguageItemPresenter languageItemPresenter = new LanguageItemPresenterImpl(languageList, resourcesManager);
        LanguagesViewAdapter adapter = new LanguagesViewAdapter(languageItemPresenter, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLanguageDetails(String languageCode) {
        Intent intent = new Intent(getContext(), LanguageCard.class);
        intent.putExtra(LANGUAGE_CODE, languageCode);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        presenter.onLanguageItemClicked(recyclerView.getChildLayoutPosition(view));
    }
}

