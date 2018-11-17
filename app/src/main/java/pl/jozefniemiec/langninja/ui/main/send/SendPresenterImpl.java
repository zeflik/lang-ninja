package pl.jozefniemiec.langninja.ui.main.send;

public class SendPresenterImpl implements SendFragmentContract.Presenter {

    private SendFragmentContract.View view;

    SendPresenterImpl(SendFragmentContract.View view) {
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
