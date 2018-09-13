package com.example.joseph.langninja.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joseph.langninja.R;
import com.example.joseph.langninja.model.Language;

import java.util.List;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    public static final String LANGUAGE_NAME_KEY = "LanguageName";
    private List<Language> languages;
    private String[] languageNames;
    private String[] languageNativeNames;
    private RecyclerView recyclerView;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView languageNameTv;
        private TextView languageNativeNameTv;
        private ImageView languageFlag;

        ViewHolder(View itemView) {
            super(itemView);
            languageNameTv = itemView.findViewById(R.id.tvLanguageName);
            languageNativeNameTv = itemView.findViewById(R.id.tvNativeLanguageName);
            languageFlag = itemView.findViewById(R.id.ivFlagOnList);
        }
    }

    public HomeRecyclerViewAdapter(RecyclerView recyclerView, String[] languageNames, String[] languageNativeNames) {
        this.recyclerView = recyclerView;
        this.languageNames = languageNames;
        this.languageNativeNames = languageNativeNames;
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_card_item, parent, false);
//        view.setOnClickListener(v -> {
//            int position = recyclerView.getChildAdapterPosition(v);
//            Intent intent = new Intent(context, LanguageCard.class);
//            intent.putExtra(LANGUAGE_NAME_KEY, position);
//            context.startActivity(intent);
//        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.languageNameTv.setText(languageNames[position]);
        holder.languageNativeNameTv.setText(languageNativeNames[position]);
        int id = context
                .getResources()
                .getIdentifier(
                        languages.get(position).getCode().toLowerCase(),
                        "drawable", context.getPackageName()
                );
        holder.languageFlag.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public void setData(List<Language> tasks) {
        this.languages = tasks;
        notifyDataSetChanged();
    }

}
