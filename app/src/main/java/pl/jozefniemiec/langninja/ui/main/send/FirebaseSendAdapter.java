package pl.jozefniemiec.langninja.ui.main.send;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;
import pl.jozefniemiec.langninja.utils.Utility;

class FirebaseSendAdapter extends FirebaseRecyclerAdapter<SentenceCandidate, SentenceRowHolder> {

    FirebaseSendAdapter(FirebaseRecyclerOptions<SentenceCandidate> recyclerOptions) {
        super(recyclerOptions);
    }

    @NonNull
    @Override
    public SentenceRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_sentence_row, parent, false);
        return new SentenceRowHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull SentenceRowHolder holder, int position, @NonNull SentenceCandidate model) {
        holder.setFlag(Utility.getLanguageFlagUri(holder.flag.getContext(), model.getLanguageCode()));
        holder.setSentence(model.getSentence());
    }
}