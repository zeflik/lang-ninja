package pl.jozefniemiec.langninja.ui.main.send.creator;

public interface SentenceCreatorContract {

    interface View {

        void close();
    }

    interface Presenter {

        void onViewCreated();

        void createButtonClicked(String langCode, String sentence);
    }
}
