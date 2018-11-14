package pl.jozefniemiec.langninja.ui.main.send;

public interface SendFragmentView {

    void showData();

    void listenForNewData();

    void stopListenForNewData();

    void showLoginInfo();

    void hideLoginInfo();

    void showLoginPage();
}
