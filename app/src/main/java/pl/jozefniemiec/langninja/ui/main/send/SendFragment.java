package pl.jozefniemiec.langninja.ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;
import pl.jozefniemiec.langninja.ui.sentence.NewSentence;

public class SendFragment extends Fragment implements SendFragmentView {

    private static final String TAG = SendFragment.class.getSimpleName();

    @BindView(R.id.floatingActionButtonAddSentence)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.sendSentencesRecyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private FirebaseRecyclerAdapter<SentenceCandidate, SentenceRowHolder> adapter;
    private SendPresenter presenter;

    private DatabaseReference dbSentencesRef = FirebaseDatabase.getInstance().getReference("sentence");

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new SendPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        floatingActionButton.setOnClickListener(v -> openNewSentenceActivity());
        presenter.onViewCreated();
    }

    public void showData() {
        FirebaseRecyclerOptions<SentenceCandidate> recyclerOptions = new FirebaseRecyclerOptions.Builder<SentenceCandidate>()
                .setQuery(dbSentencesRef, SentenceCandidate.class)
                .build();
        adapter = new FirebaseSendAdapter(this, recyclerOptions);
        recyclerView.setAdapter(adapter);
    }

    public void listenForNewData() {
        adapter.startListening();
    }

    public void stopListenForNewData() {
        adapter.stopListening();
    }

    private void openNewSentenceActivity() {
        Intent intent = new Intent(requireContext(), NewSentence.class);
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