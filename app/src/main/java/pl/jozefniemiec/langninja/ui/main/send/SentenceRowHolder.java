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

public class SentenceRowHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.sentenceRowImageView)
    ImageView flag;

    @BindView(R.id.sentenceRowTextView)
    TextView sentenceTextView;

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
}
