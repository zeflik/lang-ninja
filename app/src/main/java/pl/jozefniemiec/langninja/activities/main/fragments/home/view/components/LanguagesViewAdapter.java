package pl.jozefniemiec.langninja.activities.main.fragments.home.view.components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.main.fragments.home.presenter.HomeFragmentPresenter;

import static android.view.View.OnClickListener;


public class LanguagesViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final HomeFragmentPresenter presenter;
    private final RecyclerView recyclerView;
    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.onLanguageItemClicked(recyclerView.getChildLayoutPosition(view));
        }
    };

    public LanguagesViewAdapter(HomeFragmentPresenter presenter, RecyclerView recyclerView) {
        this.presenter = presenter;
        this.recyclerView = recyclerView;
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
