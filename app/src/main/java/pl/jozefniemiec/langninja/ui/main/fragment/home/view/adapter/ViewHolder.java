package pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;

public class ViewHolder extends RecyclerView.ViewHolder implements LanguageItemView {

    @BindView(R.id.tvLanguageName)
    TextView languageNameTv;
    @BindView(R.id.tvNativeLanguageName)
    TextView languageNativeNameTv;
    @BindView(R.id.ivFlagOnList)
    ImageView languageFlag;

    ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setLanguageNativeName(String nativeName) {
        languageNativeNameTv.setText(nativeName);
    }

    @Override
    public void setLanguageFlag(int resourceId) {
        languageFlag.setImageResource(resourceId);
    }

    @Override
    public void setLanguageName(String languageName) {
        languageNameTv.setText(languageName);
    }
}
