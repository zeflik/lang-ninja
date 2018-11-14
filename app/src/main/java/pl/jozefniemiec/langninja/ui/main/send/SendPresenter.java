package pl.jozefniemiec.langninja.ui.main.send;

public interface SendPresenter {

    void onViewVisible();

    void onViewInvisible();

    void loginButtonClicked();

    void onLoginSucceed(String firebaseUserName);

    void onLoginFailed();
}
