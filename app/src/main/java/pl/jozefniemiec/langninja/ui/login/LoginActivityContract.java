package pl.jozefniemiec.langninja.ui.login;

public interface LoginActivityContract {

    interface View {

        void close();

        void showAutoSignInPage();
    }

    interface Presenter {

        void onCreate();

        void onResume();

        void onPause();

        void onEmailSignInClicked();

        void onAutoSignInClicked();

        void onLoginSucceed();

        void onLoginFailed();
    }
}
