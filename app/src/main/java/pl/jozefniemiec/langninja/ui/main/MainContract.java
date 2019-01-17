package pl.jozefniemiec.langninja.ui.main;

public interface MainContract {

    interface View {

        void showFragments();

        void notifyDataChanged();
    }

    interface Presenter {

        void loadMain();

        void onMenuSignOutClicked();
    }
}
