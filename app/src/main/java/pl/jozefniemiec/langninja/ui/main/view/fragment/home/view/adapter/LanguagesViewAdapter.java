package pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenter;

import static android.view.View.OnClickListener;


public class LanguagesViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final HomeFragmentPresenter presenter;
    private final OnClickListener mOnClickListener;

    public LanguagesViewAdapter(HomeFragmentPresenter presenter, OnClickListener mOnClickListener) {
        this.presenter = presenter;
        this.mOnClickListener = mOnClickListener;
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
