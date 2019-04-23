package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.utils.Utility;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

public class CommentsViewHolder extends RecyclerView.ViewHolder {

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
    ImageView voteUpButton;

    @BindView(R.id.commentsThumbDownCommentIcon)
    ImageView voteDownButton;


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

    void setCommentLikesCount(String count) {
        this.commentLikesCount.setText(count);
    }
}
