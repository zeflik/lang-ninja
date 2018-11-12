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
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.ui.sentence.NewSentence;

public class SendFragment extends Fragment {

    private static final String TAG = SendFragment.class.getSimpleName();

    @BindView(R.id.floatingActionButtonAddSentence)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.sendSentencesRecyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        floatingActionButton.setOnClickListener(v -> openNewSentenceActivity());
        FirebaseRecyclerOptions<Sentence> recyclerOptions = new FirebaseRecyclerOptions.Builder<Sentence>()
                .setQuery(dbSentencesRef, Sentence.class)
                .build();
        FirebaseRecyclerAdapter<Sentence, SentenceRowHolder> adapter =
                new FirebaseRecyclerAdapter<Sentence, SentenceRowHolder>(recyclerOptions) {
                    @NonNull
                    @Override
                    public SentenceRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.new_sentence_row, parent, false);
                        return new SentenceRowHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SentenceRowHolder holder, int position, @NonNull Sentence model) {
                        holder.setFlag(new AndroidResourceManager(getResources()).getFlagId(model.getLanguageCode()));
                        holder.setSentence(model.getSentence());
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void openNewSentenceActivity() {
        Intent intent = new Intent(requireContext(), NewSentence.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}