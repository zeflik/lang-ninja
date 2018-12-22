package pl.jozefniemiec.langninja.ui.login;

import pl.jozefniemiec.langninja.ui.base.BaseContract;

public interface LoginActivityContract {

    interface View extends BaseContract.View {

        void login();

        void close();

        void showNeedInternetDialog();

        void showLoginErrorMessage();
    }

    interface Presenter {

        void onCreate();

        void onResume();

        void onPause();

        void onLoginSucceed();

        void onLoginFailed();

        void onMissingInternetConnection();

        void refreshInternetButtonClicked();
    }
}
