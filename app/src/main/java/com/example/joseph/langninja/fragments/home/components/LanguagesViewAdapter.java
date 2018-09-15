package com.example.joseph.langninja.fragments.home.components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joseph.langninja.R;
import com.example.joseph.langninja.fragments.home.HomeFragmentPresenter;

import static android.view.View.OnClickListener;


public class LanguagesViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final HomeFragmentPresenter presenter;
    private final RecyclerView recyclerView;
    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.onItemClicked(recyclerView.getChildLayoutPosition(view));
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
