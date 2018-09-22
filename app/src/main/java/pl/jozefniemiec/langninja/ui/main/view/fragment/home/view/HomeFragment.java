package pl.jozefniemiec.langninja.ui.main.view.fragment.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.ui.language.view.LanguageCard;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.adapter.LanguagesViewAdapter;
import pl.jozefniemiec.langninja.utils.Utility;

public class HomeFragment extends DaggerFragment implements HomeFragmentView, View.OnClickListener {

    private static final String TAG = "HomeFragment";
    public static final String LANGUAGE_CODE = "Language Code";

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    @Inject
    HomeFragmentPresenter homeFragmentPresenter;

    private Unbinder unbinder;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int numberOfColumns = Utility.calculateNoOfColumns(getContext().getResources().getDisplayMetrics());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);
        homeFragmentPresenter.loadLanguages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        LanguagesViewAdapter adapter = new LanguagesViewAdapter(homeFragmentPresenter, this);
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
        homeFragmentPresenter.onLanguageItemClicked(recyclerView.getChildLayoutPosition(view));
    }
}

