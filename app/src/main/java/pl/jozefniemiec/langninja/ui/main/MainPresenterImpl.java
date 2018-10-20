package pl.jozefniemiec.langninja.ui.main;

import javax.inject.Inject;

public class MainPresenterImpl implements MainPresenter {

    private final MainView view;

    @Inject
    public MainPresenterImpl(MainView view) {
        this.view = view;
    }

    @Override
    public void loadMain() {
        view.showFragments();
    }
}
