package pl.jozefniemiec.langninja.ui.main;

public interface MainContract {

    interface View {

        void showFragments();
    }

    interface Presenter {

        void loadMain();

        void onMenuSignOutClicked();
    }
}
