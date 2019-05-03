package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class CommentsViewHolder extends RecyclerView.ViewHolder implements CommentsItemView {

    private final Picasso picasso;

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

    @BindView(R.id.commentVoteUpProgressBar)
    ProgressBar voteUpProgressBar;

    @BindView(R.id.commentVoteDownProgressBar)
    ProgressBar voteDownProgressBar;

    CommentsViewHolder(View itemView, Picasso picasso) {
        super(itemView);
        this.picasso = picasso;
        ButterKnife.bind(this, itemView);
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
    public boolean isVoteUpButtonSelected() {
        return voteUpButton.isSelected();
    }

    @Override
    public boolean isVoteDownButtonSelected() {
        return voteDownButton.isSelected();
    }

    @Override
    public void changeLikesCountByValue(int value) {
        setCommentLikesCount(Integer.valueOf(commentLikesCount.getText().toString()) + value);
    }

    @Override
    public int getLikesCount() {
        return Integer.valueOf(commentLikesCount.getText().toString());
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

    public void hideVoteDownProgress() {
        voteDownProgressBar.setVisibility(View.GONE);
    }
}
