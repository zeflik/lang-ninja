package pl.jozefniemiec.langninja.ui.main.languages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.main.languages.LanguagesFragmentScope;
import pl.jozefniemiec.langninja.utils.Utility;

@LanguagesFragmentScope
public class LanguagesListAdapter extends RecyclerView.Adapter<LanguagesViewHolder> {

    private final LanguagesFragmentContract.Presenter presenter;
    private final Context context;
    private final ResourcesManager resourcesManager;
    private List<Language> languages;

    @Inject
    LanguagesListAdapter(Context context, LanguagesFragmentContract.Presenter presenter,
                         ResourcesManager resourcesManager) {
        this.context = context;
        this.presenter = presenter;
        this.resourcesManager = resourcesManager;
    }

    @NonNull
    @Override
    public LanguagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_card_item, parent, false);
        return new LanguagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagesViewHolder holder, int position) {
        holder.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
        holder.setLanguageNativeName(languages.get(position).getNativeName());
        Picasso
                .with(context)
                .load(Utility.getLanguageFlagUri(context, languages.get(position).getCode()))
                .into(holder.languageFlag);
        holder.itemView.setOnClickListener(view ->
                presenter.onLanguageItemClicked(languages.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public void setData(List<Language> languageList) {
        languages = languageList;
    }
}
