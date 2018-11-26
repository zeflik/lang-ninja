package pl.jozefniemiec.langninja.ui.main.send;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.utils.Utility;

class UserSentenceListAdapter extends RecyclerView.Adapter<SentenceRowHolder> {

    private List<UserSentence> userSentences = new ArrayList<>();
    private OnClickListener listener;

    UserSentenceListAdapter() {
    }

    void addUserSententes(List<UserSentence> userSentences) {
        this.userSentences.addAll(userSentences);
    }

    void addUserSentente(UserSentence userSentence) {
        this.userSentences.add(userSentence);
        notifyItemInserted(userSentences.size() - 1);
        Log.d("sdfsdfsd", "addUserSentente: add user called");
    }

    @NonNull
    @Override
    public SentenceRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SentenceRowHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_sentence_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceRowHolder holder, int position) {
        holder.setAuthor(userSentences.get(position).getCreatedBy());
        holder.setSentence(userSentences.get(position).getSentence());
        holder.setFlag(Utility.getLanguageFlagUri(holder.flag.getContext(), userSentences.get(position).getLanguageCode()));
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(userSentences.get(position)));
    }

    @Override
    public int getItemCount() {
        return userSentences.size();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onItemClicked(UserSentence userSentence);
    }
}
