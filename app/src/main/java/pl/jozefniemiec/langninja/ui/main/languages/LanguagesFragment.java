package pl.jozefniemiec.langninja.ui.main.languages;

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
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;

public class LanguagesFragment extends DaggerFragment implements LanguagesFragmentView, View.OnClickListener {

    private static final String TAG = "LanguagesFragment";
    public static final String LANGUAGE_CODE = "Language Code";

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    @Inject
    LanguagesFragmentPresenter languagesFragmentPresenter;

    @Inject
    GridLayoutManager gridLayoutManager;

    @Inject
    LanguagesViewAdapter adapter;

    private Unbinder unbinder;

    public static LanguagesFragment newInstance() {
        Bundle args = new Bundle();
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_languages_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(gridLayoutManager);
        languagesFragmentPresenter.loadLanguages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLanguageDetails(String languageCode) {
        Intent intent = new Intent(requireContext(), SentenceCard.class);
        intent.putExtra(LANGUAGE_CODE, languageCode);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        languagesFragmentPresenter.onLanguageItemClicked(recyclerView.getChildLayoutPosition(view));
    }
}

