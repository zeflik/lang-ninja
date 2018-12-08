package pl.jozefniemiec.langninja.ui.main.languages;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;

class LanguagesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvLanguageName)
    TextView languageNameTv;

    @BindView(R.id.tvNativeLanguageName)
    TextView languageNativeNameTv;

    @BindView(R.id.ivFlagOnList)
    ImageView languageFlag;

    LanguagesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void setLanguageNativeName(String nativeName) {
        languageNativeNameTv.setText(nativeName);
    }

    void setLanguageName(String languageName) {
        languageNameTv.setText(languageName);
    }
}
