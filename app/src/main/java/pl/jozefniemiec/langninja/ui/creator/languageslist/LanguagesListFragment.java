package pl.jozefniemiec.langninja.ui.creator.languageslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.base.BaseLanguagesListFragment;

public class LanguagesListFragment extends BaseLanguagesListFragment
        implements LanguagesListContract.View {

    private static final String LISTENER_ERROR_MESSAGE = "Parent activity should implement LanguagesListListener";
    private LanguagesListListener listener;

    @Inject
    LanguagesListContract.Presenter presenter;

    public static LanguagesListFragment newInstance() {
        Bundle args = new Bundle();
        LanguagesListFragment fragment = new LanguagesListFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onItemClicked(Language language) {
        listener.onLanguagesListResult(language);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}