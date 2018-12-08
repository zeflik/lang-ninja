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
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;

public class LanguagesFragment extends DaggerFragment implements LanguagesFragmentContract.View {

    private Unbinder unbinder;

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    @Inject
    LanguagesFragmentContract.Presenter presenter;

    @Inject
    LanguagesListAdapter adapter;

    public static LanguagesFragment newInstance() {
        Bundle args = new Bundle();
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_languages_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(requireActivity(), Utility.calculateNoOfColumns(requireActivity()));
        recyclerView.setLayoutManager(gridLayoutManager);
        presenter.onViewCreated();
    }

    @Override
    public void showLanguages(List<Language> languageList) {
        adapter.setData(languageList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLanguageDetails(String languageCode) {
        Intent intent = new Intent(requireContext(), SentenceCardViewerActivity.class);
        intent.putExtra(LANGUAGE_CODE_KEY, languageCode);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

