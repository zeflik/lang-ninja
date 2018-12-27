package pl.jozefniemiec.langninja.ui.main.community;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

public class SentenceRowHolder extends RecyclerView.ViewHolder implements UserSentenceItemView {

    @BindView(R.id.sentenceRowImageView)
    ImageView flag;

    @BindView(R.id.sentenceRowSentenceTextView)
    TextView sentenceTextView;

    @BindView(R.id.sentenceRowAuthorTextView)
    TextView authorTextView;

    @BindView(R.id.newSentenceRowUserPhoto)
    ImageView authorPhoto;

    SentenceRowHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setFlag(Uri uri) {
        Picasso
                .with(itemView.getContext())
                .load(uri)
                .into(flag);
    }

    public void setSentence(String sentence) {
        this.sentenceTextView.setText(sentence);
    }

    public void setAuthor(String name) {
        authorTextView.setText(name);
    }

    public void setAuthorPhoto(Uri uri) {
        Picasso
                .with(itemView.getContext())
                .load(uri)
                .fit()
                .transform(new CircleTransform())
                .into(authorPhoto);
    }
}
