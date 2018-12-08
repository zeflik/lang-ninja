package pl.jozefniemiec.langninja.ui.creator;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;

public class SpinnerRowHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.spinner_flag)
    ImageView flag;

    @BindView(R.id.spinner_lang_name)
    TextView langName;

    SpinnerRowHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setFlag(Uri uri) {
        Picasso
                .with(itemView.getContext())
                .load(uri)
                .into(flag);
    }

    public void setLangName(String langName) {
        this.langName.setText(langName);
    }

    public ImageView getFlag() {
        return flag;
    }

    public TextView getLangName() {
        return langName;
    }
}
