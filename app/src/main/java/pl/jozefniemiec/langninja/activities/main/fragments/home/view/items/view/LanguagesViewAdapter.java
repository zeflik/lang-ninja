package pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.view;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.presenter.LanguageItemPresenter;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.presenter.LanguageItemPresenterImpl;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.resources.ResourcesManager;

import static android.view.View.OnClickListener;


public class LanguagesViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final LanguageItemPresenter presenter;
    private final OnClickListener mOnClickListener;

    public LanguagesViewAdapter(Resources resources, List<Language> languages, OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        ResourcesManager resourcesManager = new AndroidResourceManager(resources);
        presenter = new LanguageItemPresenterImpl(languages, resourcesManager);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_card_item, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presenter.onBindLanguageItemViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getLanguageItemsCount();
    }

}
