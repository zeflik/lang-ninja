package pl.jozefniemiec.langninja.ui.main;

import javax.inject.Inject;

public class MainPresenterImpl implements MainContract.Presenter {

    private final MainContract.View view;

    @Inject
    public MainPresenterImpl(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void loadMain() {
        view.showFragments();
    }
}
