package pl.jozefniemiec.langninja.ui.main.languages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.base.BaseLanguagesListFragment;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;

public class LanguagesFragment extends BaseLanguagesListFragment implements LanguagesFragmentContract.View {

    public static final String LANGUAGE_CODE_KEY = "Language Code";

    @Inject
    LanguagesFragmentContract.Presenter presenter;

    public static LanguagesFragment newInstance() {
        Bundle args = new Bundle();
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void showLanguageDetails(String languageCode) {
        Intent intent = new Intent(requireContext(), SentenceCard.class);
        intent.putExtra(LANGUAGE_CODE_KEY, languageCode);
        startActivity(intent);
    }

    @Override
    protected void onItemClicked(Language language) {
        presenter.onLanguageItemClicked(language);
    }
}

