package pl.jozefniemiec.langninja.activities.main.fragments.home;

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
import pl.jozefniemiec.langninja.activities.main.fragments.home.components.LanguagesViewAdapter;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.utils.Utility;

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
        AndroidResourceManager androidResourceManager = new AndroidResourceManager(getResources());
        homeFragmentPresenter = new HomeFragmentPresenter(this, new RoomLanguageRepository(getContext()), androidResourceManager);
        homeFragmentPresenter.loadLanguages();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        adapter = new LanguagesViewAdapter(homeFragmentPresenter, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLanguageDetails(String languageCode) {
        Toast.makeText(getContext(), "creating activity " + languageCode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
