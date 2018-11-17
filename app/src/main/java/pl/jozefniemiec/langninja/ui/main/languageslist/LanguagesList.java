package pl.jozefniemiec.langninja.ui.main.languageslist;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.base.BaseLanguagesListFragment;

public class LanguagesList extends BaseLanguagesListFragment {

    private static final String LISTENER_ERROR_MESSAGE = "Parent activity should implement LanguagesListListener";
    private LanguagesListListener listener;

    public static LanguagesList newInstance() {
        Bundle args = new Bundle();
        LanguagesList fragment = new LanguagesList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            listener = (LanguagesListListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(LISTENER_ERROR_MESSAGE);
        }
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        showLanguages(new ArrayList<>(Arrays.asList(new Language("th_TH", "dupa"))));
    }

    @Override
    protected void onItemClicked(Language language) {
        listener.onLanguagesListResult(language);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
