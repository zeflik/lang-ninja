package pl.jozefniemiec.langninja.ui.main.fragment.home.view;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.ui.language.view.LanguageCard;
import pl.jozefniemiec.langninja.ui.main.fragment.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.fragment.home.presenter.HomeFragmentPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.view.LanguagesViewAdapter;
import pl.jozefniemiec.langninja.utils.Utility;

public class HomeFragment extends Fragment implements HomeFragmentView, View.OnClickListener {

    private static final String TAG = "HomeFragment";
    public static final String LANGUAGE_CODE = "Language Code";

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;
    private HomeFragmentPresenter presenter;
    private Unbinder unbinder;

    @Inject
    public HomeFragment() {
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
        int numberOfColumns = Utility.calculateNoOfColumns(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);
        LanguageRepository roomLanguageRepository = new RoomLanguageRepository(getContext());
        presenter = new HomeFragmentPresenterImpl(this, roomLanguageRepository);
        presenter.loadLanguages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        LanguagesViewAdapter adapter = new LanguagesViewAdapter(getContext().getResources(), languageList, this);
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

