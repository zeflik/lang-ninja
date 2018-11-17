package pl.jozefniemiec.langninja.ui.creator.languageslist;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.creator.languageslist.LanguagesListFragmentScope;

@LanguagesListFragmentScope
public class LanguagesListPresenter implements LanguagesListContract.Presenter {

    private final LanguagesListContract.View view;

    @Inject
    LanguagesListPresenter(LanguagesListContract.View view) {
        this.view = view;
    }
}
