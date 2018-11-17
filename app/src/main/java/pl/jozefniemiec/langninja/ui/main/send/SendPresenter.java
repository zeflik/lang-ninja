package pl.jozefniemiec.langninja.ui.main.send;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.main.send.SendFragmentScope;

@SendFragmentScope
public class SendPresenter implements SendFragmentContract.Presenter {

    private SendFragmentContract.View view;

    @Inject
    SendPresenter(SendFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewVisible() {
        view.listenForNewData();
    }

    @Override
    public void onViewInvisible() {
        view.stopListenForNewData();
    }

    @Override
    public void loginButtonClicked() {
        view.showLoginPage();
    }

    @Override
    public void onLoginSucceed(String firebaseUserName) {
        view.hideLoginInfo();
        view.showData();
    }

    @Override
    public void onLoginFailed() {
        view.showLoginInfo();
    }
}
