package pl.jozefniemiec.langninja.ui.main.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.room.RoomLanguageRepository;
import pl.jozefniemiec.langninja.ui.base.spinner.LanguagesSpinnerAdapter;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;

import static pl.jozefniemiec.langninja.ui.base.Constants.DEFAULT_LANG_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class CommunityFragment extends DaggerFragment implements CommunityFragmentContract.View {

    private static final String TAG = CommunityFragment.class.getSimpleName();
    private Unbinder unbinder;
    private int spinnerOnSelectedCounter;

    @Inject
    UserSentenceListAdapter adapter;

    @BindView(R.id.floatingActionButtonAddSentence)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.sendSentencesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.sentencesCategoryFilterSpinner)
    Spinner sentencesCategoryFilterSpinner;

    @BindView(R.id.sentenceLanguageFilterSpinner)
    Spinner sentenceLanguageFilterSpinner;

    @Inject
    CommunityFragmentContract.Presenter presenter;

    @OnItemSelected({R.id.sentenceLanguageFilterSpinner, R.id.sentencesCategoryFilterSpinner})
    public void onSpinnerChanged(Spinner spinner, int position) {
        if (spinnerOnSelectedCounter++ > 0) {
            presenter.onOptionSelected((Language) sentenceLanguageFilterSpinner.getSelectedItem(),
                                       sentencesCategoryFilterSpinner.getSelectedItemPosition());
        }
    }

    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_community, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter.setListener(userSentence -> {
            Intent intent = new Intent(requireActivity(), SentenceCardViewerActivity.class);
            intent.putExtra(LANGUAGE_CODE_KEY, userSentence.getLanguageCode());
            intent.putExtra(SENTENCE_KEY, userSentence.getSentence());
            intent.putExtra(SENTENCE_ID_KEY, userSentence.getId());
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(v -> openNewSentencePage());
        initializeSpinners();
    }

    private void initializeSpinners() {
        String[] spinnerOptions = getResources().getStringArray(R.array.filter_spinner_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.simple_spinner_row, R.id.simple_spinner_textview, spinnerOptions);
        sentencesCategoryFilterSpinner.setAdapter(adapter);
        List<Language> languages = new RoomLanguageRepository(requireContext()).getAll();
        LanguagesSpinnerAdapter languagesSpinnerAdapter = new LanguagesSpinnerAdapter(requireContext());
        languagesSpinnerAdapter.add(new Language(DEFAULT_LANG_KEY, getString(R.string.spinner_all_languages)));
        languagesSpinnerAdapter.addAll(languages);
        sentenceLanguageFilterSpinner.setAdapter(languagesSpinnerAdapter);
    }

    @Override
    public void addData(UserSentence userSentence) {
        adapter.addUserSentence(userSentence);
        if (recyclerView != null) {
            recyclerView.scrollToPosition(UserSentenceListAdapter.INSERT_INDEX);
        }
    }

    @Override
    public void clearData() {
        adapter.removeAll();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void openNewSentencePage() {
        Intent intent = new Intent(requireContext(), SentenceCreator.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroyView();
    }
}