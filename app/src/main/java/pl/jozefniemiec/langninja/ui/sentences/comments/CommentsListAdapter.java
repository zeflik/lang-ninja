package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.utils.DateUtils;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsViewHolder> {

    private final Picasso picasso;
    private final Context context;
    private final CommentsFragmentContract.Presenter presenter;
    private List<Comment> comments = new ArrayList<>();

    @Inject
    public CommentsListAdapter(Picasso picasso, Context context, CommentsFragmentContract.Presenter presenter) {
        this.picasso = picasso;
        this.context = context;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new CommentsViewHolder(view, picasso);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.setAuthor(comments.get(position).getAuthor().getName());
        holder.setComment(comments.get(position).getContent());
        holder.setAuthorPhoto(Uri.parse(comments.get(position).getAuthor().getPhoto()));
        holder.setCommentLikesCount(comments.get(position).getLikes().getCount());
        Long timestamp = (Long) comments.get(position).getDateEdited();
        String timeAgo = DateUtils.generateTimePeriodDescription(timestamp, context);
        holder.setDateText(timeAgo);
        holder.voteUpButton.setOnClickListener(view -> presenter.onVoteUpButtonClicked(holder, comments.get(position)));
        holder.voteDownButton.setOnClickListener(view -> presenter.onVoteDownButtonClicked(holder, comments.get(position)));
        holder.commentsReplayButton.setOnClickListener(view -> presenter.onItemViewReplayButtonPressed(holder));
        presenter.onItemViewLikesBind(holder, comments.get(position).getLikes());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    public void addItem(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }
}
