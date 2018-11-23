package pl.jozefniemiec.langninja.ui.base;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.utils.Utility;

public abstract class BaseLanguagesListFragment extends DaggerFragment implements View.OnClickListener {

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    BaseLanguagesListAdapter adapter = new BaseLanguagesListAdapter(this);
    private List<Language> languages;
    private Unbinder unbinder;

    protected abstract void onItemClicked(Language language);

    public void showLanguages(List<Language> languageList) {
        adapter.setData(languageList);
        recyclerView.setAdapter(adapter);
    }

    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_languages_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(requireActivity(), Utility.calculateNoOfColumns(requireActivity()));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        int index = recyclerView.getChildLayoutPosition(view);
        onItemClicked(languages.get(index));
    }

    class BaseLanguagesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvLanguageName)
        TextView languageNameTv;
        @BindView(R.id.tvNativeLanguageName)
        TextView languageNativeNameTv;
        @BindView(R.id.ivFlagOnList)
        ImageView languageFlag;

        BaseLanguagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setLanguageNativeName(String nativeName) {
            languageNativeNameTv.setText(nativeName);
        }

        public void setLanguageFlag(Uri uri) {
            Picasso
                    .with(requireContext())
                    .load(uri)
                    .noFade()
                    .into(languageFlag);
        }

        public void setLanguageName(String languageName) {
            languageNameTv.setText(languageName);
        }
    }

    class BaseLanguagesListAdapter extends RecyclerView.Adapter<BaseLanguagesViewHolder> {

        private final View.OnClickListener onClickListener;

        BaseLanguagesListAdapter(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @NonNull
        @Override
        public BaseLanguagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.language_card_item, parent, false);
            view.setOnClickListener(onClickListener);
            return new BaseLanguagesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseLanguagesViewHolder holder, int position) {
            holder.setLanguageName(languages.get(position).getCode());
            holder.setLanguageNativeName(languages.get(position).getNativeName());
            holder.setLanguageFlag(
                    Utility.getLanguageFlagUri(requireContext(), languages.get(position).getCode())
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
}
