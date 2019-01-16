package pl.jozefniemiec.langninja.ui.main.community;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import static pl.jozefniemiec.langninja.ui.base.Constants.ACTION_USER_SENTENCES_CHANGED;
import static pl.jozefniemiec.langninja.ui.base.Constants.DEFAULT_LANG_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class CommunityFragment extends DaggerFragment implements CommunityFragmentContract.View {

    private static final String TAG = CommunityFragment.class.getSimpleName();
    public static final int SPINNERS_INITIAL_AUTO_TRIGGER = 2;
    private Unbinder unbinder;
    private int spinnerOnSelectedCounter;
    @Inject
    UserSentenceListAdapter adapter;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.pullData((Language) sentenceLanguageFilterSpinner.getSelectedItem(),
                               sentencesCategoryFilterSpinner.getSelectedItemPosition());
        }
    };

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
        if (spinnerOnSelectedCounter++ > SPINNERS_INITIAL_AUTO_TRIGGER) {
            presenter.pullData((Language) sentenceLanguageFilterSpinner.getSelectedItem(),
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter.setListener(userSentence -> presenter.onShowButtonClicked(userSentence));
        adapter.setOnLongClickListener(userSentence -> presenter.onItemLongButtonClicked(userSentence));
        return view;

    }

    @Override
    public void showSentenceDetails(UserSentence userSentence) {
        Intent intent = new Intent(requireActivity(), SentenceCardViewerActivity.class);
        intent.putExtra(LANGUAGE_CODE_KEY, userSentence.getLanguageCode());
        intent.putExtra(SENTENCE_KEY, userSentence.getSentence());
        intent.putExtra(SENTENCE_ID_KEY, userSentence.getId());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
        initializeSpinners();
        floatingActionButton.setOnClickListener(v -> {
            Language currentLanguage = (Language) sentenceLanguageFilterSpinner.getSelectedItem();
            presenter.onCreateSentenceButtonClicked(currentLanguage);
        });
        presenter.onViewCreated();
        presenter.pullData((Language) sentenceLanguageFilterSpinner.getSelectedItem(),
                           sentencesCategoryFilterSpinner.getSelectedItemPosition());
    }

    @Override
    public void registerOnDataChangeListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USER_SENTENCES_CHANGED);
        requireActivity().registerReceiver(broadcastReceiver, filter);
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void addData(List<UserSentence> userSentences) {
        adapter.addAll(userSentences);
    }

    @Override
    public void clearData() {
        adapter.removeAll();
    }

    @Override
    public void showSentenceOptionsDialog(CharSequence[] options, UserSentence userSentence) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(userSentence.getSentence());
        builder.setItems(options, (dialog, item) -> presenter.onSentenceOptionSelected(item, userSentence));
        builder.show();
    }

    @Override
    public void showInappropriateContentDialog(CharSequence[] reasons, UserSentence userSentence) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(
                String.format(
                        getString(R.string.alert_title_send_sentence_feedback),
                        userSentence.getSentence()
                ));
        builder.setItems(reasons, (dialog, item) -> presenter.onInappropriateContentSelected(item, userSentence));
        builder.show();
    }

    @Override
    public void removeItem(UserSentence userSentence) {
        adapter.remove(userSentence);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void openNewSentencePage(String languageCode) {
        Intent intent = new Intent(requireContext(), SentenceCreator.class);
        intent.putExtra(LANGUAGE_CODE_KEY, languageCode);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroyView();
    }

    @Override
    public void unregisterOnDataChangeListener() {
        requireActivity().unregisterReceiver(broadcastReceiver);
    }
}