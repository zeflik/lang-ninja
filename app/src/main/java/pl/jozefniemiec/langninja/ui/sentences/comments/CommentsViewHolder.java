package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.utils.Utility;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

public class CommentsViewHolder extends RecyclerView.ViewHolder implements CommentsItemView, View.OnClickListener, View.OnLongClickListener {

    private final Picasso picasso;
    private ViewHolderClicks listener;

    @BindView(R.id.commentAuthorTextView)
    TextView author;

    @BindView(R.id.commentBodyTextView)
    TextView comment;

    @BindView(R.id.commentAuthorPhoto)
    ImageView authorPhoto;

    @BindView(R.id.commentCalendarTextView)
    TextView dateText;

    @BindView(R.id.commentLikesCountTextView)
    TextView commentLikesCount;

    @BindView(R.id.commentsThumbUpCommentIcon)
    ImageButton voteUpButton;

    @BindView(R.id.commentsThumbDownCommentIcon)
    ImageButton voteDownButton;

    @BindView(R.id.commentsReplayButton)
    Button commentsReplayButton;

    @BindView(R.id.commentVoteUpProgressBar)
    ProgressBar voteUpProgressBar;

    @BindView(R.id.commentVoteDownProgressBar)
    ProgressBar voteDownProgressBar;

    @BindView(R.id.commentsReplaysRecyclerView)
    RecyclerView replaysRecyclerView;

    @BindView(R.id.commentBackground)
    View commentBackground;

    CommentsViewHolder(View itemView, Picasso picasso, ViewHolderClicks listener) {
        super(itemView);
        this.picasso = picasso;
        this.listener = listener;
        ButterKnife.bind(this, itemView);
        voteUpButton.setOnClickListener(this);
        voteDownButton.setOnClickListener(this);
        commentBackground.setOnLongClickListener(this);
    }

    public void setAuthor(String name) {
        this.author.setText(name);
    }

    public void setComment(String text) {
        this.comment.setText(text);
    }

    void setAuthorPhoto(Uri uri) {
        picasso
                .load(uri)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .transform(new CircleTransform())
                .into(authorPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        if (Utility.isNetworkAvailable(itemView.getContext())) {
                            picasso
                                    .load(uri)
                                    .fit()
                                    .transform(new CircleTransform())
                                    .into(authorPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });
                        }
                    }
                });
    }

    void setDateText(String dateText) {
        this.dateText.setText(dateText);
    }

    void setCommentLikesCount(int count) {
        this.commentLikesCount.setText(String.valueOf(count));
    }

    @Override
    public void indicateNegativeNumber() {
        commentLikesCount.setTextColor(Color.RED);
    }

    @Override
    public void indicatePositiveNumber() {
        commentLikesCount.setTextColor(Color.GRAY);
    }

    @Override
    public void selectVoteUpButton(boolean state) {
        voteUpButton.setSelected(state);
    }

    @Override
    public void selectVoteDownButton(boolean state) {
        voteDownButton.setSelected(state);
    }

    @Override
    public void showVoteUpProgress() {
        voteUpProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoteUpProgress() {
        voteUpProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showVoteDownProgress() {
        voteDownProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoteDownProgress() {
        voteDownProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showReplaysList() {
        replaysRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReplaysList() {
        replaysRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void resetViewState() {
        voteUpButton.setSelected(false);
        voteDownButton.setSelected(false);
        indicatePositiveNumber();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commentsThumbUpCommentIcon:
                listener.onVoteUp(this, getAdapterPosition());
                break;
            case R.id.commentsThumbDownCommentIcon:
                listener.onVoteDown(this, getAdapterPosition());
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        listener.onBackground(getAdapterPosition());
        return true;
    }

    public interface ViewHolderClicks {
        void onVoteUp(CommentsItemView holder, int position);

        void onVoteDown(CommentsItemView holder, int position);

        void onBackground(int position);
    }
}
