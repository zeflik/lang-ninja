package pl.jozefniemiec.langninja.ui.main.presenter;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.ui.main.view.MainView;

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
