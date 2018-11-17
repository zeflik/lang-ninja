package pl.jozefniemiec.langninja.ui.main.send;

public interface SendFragmentContract {

    interface View {

        void showData();

        void listenForNewData();

        void stopListenForNewData();

        void showLoginInfo();

        void hideLoginInfo();

        void showLoginPage();
    }

    interface Presenter {

        void onViewVisible();

        void onViewInvisible();

        void loginButtonClicked();

        void onLoginSucceed(String firebaseUserName);

        void onLoginFailed();
    }

}
