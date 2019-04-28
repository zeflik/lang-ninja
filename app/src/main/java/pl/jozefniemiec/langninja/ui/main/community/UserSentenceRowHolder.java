package pl.jozefniemiec.langninja.ui.main.community;

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

import static pl.jozefniemiec.langninja.utils.Utility.changeTextViewColorIfNegativeNumber;

public class UserSentenceRowHolder extends RecyclerView.ViewHolder implements UserSentenceItemView {

    private final Picasso picasso;

    @BindView(R.id.sentenceRowImageView)
    ImageView flag;

    @BindView(R.id.sentenceRowSentenceTextView)
    TextView sentenceTextView;

    @BindView(R.id.sentenceRowAuthorTextView)
    TextView authorTextView;

    @BindView(R.id.newSentenceRowUserPhoto)
    ImageView authorPhoto;

    @BindView(R.id.newSentenceRowThumbsTextView)
    TextView likesCountTextView;

    @BindView(R.id.newSentenceRowCalendarTextView)
    TextView newSentenceRowCalendarTextView;


    UserSentenceRowHolder(View itemView, Picasso picasso) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.picasso = picasso;
    }

    public void setFlag(Uri uri) {
        picasso
                .load(uri)
                .into(flag);
    }

    public void setSentence(String sentence) {
        this.sentenceTextView.setText(sentence);
    }

    public void setAuthor(String name) {
        authorTextView.setText(name);
    }

    public void setLikesCountTextView(String value) {
        likesCountTextView.setText(value);
        changeTextViewColorIfNegativeNumber(likesCountTextView, Integer.valueOf(value));
    }

    public void setDateText(String timeAgo) {
        newSentenceRowCalendarTextView.setText(timeAgo);
    }

    public void setAuthorPhoto(Uri uri) {
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
}
