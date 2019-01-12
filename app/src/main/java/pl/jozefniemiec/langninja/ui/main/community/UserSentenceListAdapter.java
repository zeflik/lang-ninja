package pl.jozefniemiec.langninja.ui.main.community;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.utils.DateUtils;
import pl.jozefniemiec.langninja.utils.Utility;

class UserSentenceListAdapter extends RecyclerView.Adapter<UserSentenceRowHolder> {

    public static final String TAG = UserSentenceListAdapter.class.getSimpleName();
    private List<UserSentence> userSentences = new ArrayList<>();
    private OnClickListener listener;
    private OnLongClickListener onLongClickListener;
    private Context context;
    private Picasso picasso;

    UserSentenceListAdapter() {
    }

    @Inject
    UserSentenceListAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
    }

    void addUserSentence(UserSentence userSentence) {
        this.userSentences.add(userSentence);
        notifyDataSetChanged();
    }

    void removeAll() {
        userSentences.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserSentenceRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserSentenceRowHolder(LayoutInflater.from(parent.getContext())
                                                 .inflate(R.layout.new_sentence_row, parent, false), picasso);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSentenceRowHolder holder, int position) {
        holder.setAuthor(userSentences.get(position).getAuthor().getName());
        holder.setSentence(userSentences.get(position).getSentence());
        holder.setFlag(Utility.getLanguageFlagUri(holder.flag.getContext(), userSentences.get(position).getLanguageCode()));
        holder.setAuthorPhoto(Uri.parse(userSentences.get(position).getAuthor().getPhoto()));
        holder.setLikesCountTextView(String.valueOf(userSentences.get(position).getLikesCount()));
        Long timestamp = (Long) userSentences.get(position).getDateEdited();
        String timeAgo = DateUtils.generateTimePeriodDescription(timestamp, context);
        holder.setDateText(timeAgo);
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(userSentences.get(position)));
        if (onLongClickListener != null) {
            holder.itemView.setOnLongClickListener(view -> {
                onLongClickListener.onLongItemClicked(userSentences.get(position));
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return userSentences.size();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnClickListener {
        void onItemClicked(UserSentence userSentence);
    }

    public interface OnLongClickListener {
        void onLongItemClicked(UserSentence userSentence);
    }
}
