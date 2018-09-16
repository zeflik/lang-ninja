package pl.jozefniemiec.langninja.activities.main.fragments.home.view.components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pl.jozefniemiec.langninja.R;

public class ViewHolder extends RecyclerView.ViewHolder implements LanguageItemView {
    private TextView languageNameTv;
    private TextView languageNativeNameTv;
    private ImageView languageFlag;

    ViewHolder(View itemView) {
        super(itemView);
        languageNameTv = itemView.findViewById(R.id.tvLanguageName);
        languageNativeNameTv = itemView.findViewById(R.id.tvNativeLanguageName);
        languageFlag = itemView.findViewById(R.id.ivFlagOnList);
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
