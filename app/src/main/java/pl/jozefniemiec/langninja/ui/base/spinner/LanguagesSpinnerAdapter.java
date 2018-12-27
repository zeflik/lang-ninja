package pl.jozefniemiec.langninja.ui.base.spinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Objects;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.utils.Utility;

@SentenceCreatorScope
public class LanguagesSpinnerAdapter extends ArrayAdapter<Language> {

    @Inject
    public LanguagesSpinnerAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.languages_spinner_row, parent, false);
        }
        LanguagesSpinnerRowHolder holder = new LanguagesSpinnerRowHolder(convertView);
        holder.setFlag(Utility.getLanguageFlagUri(getContext(), Objects.requireNonNull(getItem(position)).getCode()));
        holder.setLangName(Objects.requireNonNull(getItem(position)).getNativeName());
        return convertView;
    }
}
