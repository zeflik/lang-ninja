package pl.jozefniemiec.langninja.ui.main.send;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.UserSentence;
import pl.jozefniemiec.langninja.utils.Utility;

class FirebaseSendAdapter extends FirebaseRecyclerAdapter<UserSentence, SentenceRowHolder> {

    private static final String TAG = FirebaseSendAdapter.class.getSimpleName();

    private OnClickListener listener;

    FirebaseSendAdapter(FirebaseRecyclerOptions<UserSentence> recyclerOptions) {
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
    protected void onBindViewHolder(@NonNull SentenceRowHolder holder, int position, @NonNull UserSentence model) {
        holder.setFlag(Utility.getLanguageFlagUri(holder.flag.getContext(), model.getLanguageCode()));
        holder.setSentence(model.getSentence());
        getRef(position).getRoot().child("users/" + model.getCreatedBy() + "/name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.setAuthor(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(model));
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onItemClicked(UserSentence sentenceRowHolder);
    }
}
