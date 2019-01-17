package pl.jozefniemiec.langninja.ui.main;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;
    private final FirebaseAuth auth;

    @Inject
    public MainPresenter(MainContract.View view, FirebaseAuth auth) {
        this.view = view;
        this.auth = auth;
    }

    @Override
    public void loadMain() {
        view.showFragments();
    }

    @Override
    public void onMenuSignOutClicked() {
        auth.signOut();
        view.notifyDataChanged();
    }
}
