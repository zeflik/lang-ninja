package pl.jozefniemiec.langninja.ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SendFragment extends DaggerFragment implements SendFragmentContract.View {

    private static final String TAG = SendFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;

    @BindView(R.id.floatingActionButtonAddSentence)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.sendSentencesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.logoffButton)
    Button logoffButton;

    @Inject
    SendFragmentContract.Presenter presenter;

    private Unbinder unbinder;
    private UserSentenceListAdapter adapter = new UserSentenceListAdapter();
    private FirebaseAuth auth;

    private DatabaseReference dbSentencesRef = FirebaseDatabase.getInstance().getReference("sentence");

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_send, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter.setListener(userSentence -> {
            Intent intent = new Intent(requireActivity(), SentenceCardViewerActivity.class);
            intent.putExtra(LANGUAGE_CODE_KEY, userSentence.getLanguageCode());
            intent.putExtra(SENTENCE_KEY, userSentence.getSentence());
            intent.putExtra(SENTENCE_ID_KEY, userSentence.getId());
            startActivity(intent);
        });
        presenter.loadData(null);
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
        auth = FirebaseAuth.getInstance();
        logoffButton.setOnClickListener(v -> auth.signOut());
    }

    public void showData(List<UserSentence> userSentences) {
        Log.d(TAG, "onDataChange called form view" + userSentences);
        adapter.addUserSententes(userSentences);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addData(UserSentence userSentence) {
        adapter.addUserSentente(userSentence);
    }

    private void openNewSentencePage() {
        Intent intent = new Intent(requireContext(), SentenceCreator.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewInvisible();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}