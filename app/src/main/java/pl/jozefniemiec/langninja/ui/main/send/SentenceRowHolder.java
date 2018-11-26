package pl.jozefniemiec.langninja.ui.main.send;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;

public class SentenceRowHolder extends RecyclerView.ViewHolder implements UserSentenceItemView {

    @BindView(R.id.sentenceRowImageView)
    ImageView flag;

    @BindView(R.id.sentenceRowSentenceTextView)
    TextView sentenceTextView;

    @BindView(R.id.sentenceRowAuthorTextView)
    TextView authorTextView;

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
}
