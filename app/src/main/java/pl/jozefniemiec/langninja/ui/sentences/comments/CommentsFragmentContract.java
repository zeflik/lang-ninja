package pl.jozefniemiec.langninja.ui.sentences.comments;

public interface CommentsFragmentContract {

    interface View {

        void close();
    }

    interface Presenter {

        void onBackgroundClicked();
    }
}
