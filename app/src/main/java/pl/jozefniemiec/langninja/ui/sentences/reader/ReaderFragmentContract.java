package pl.jozefniemiec.langninja.ui.sentences.reader;

import java.util.Locale;

public interface ReaderFragmentContract {

    interface View {

        boolean setReaderLanguage(Locale locale);

        void activateReadButton();

        void deactivateReadButton();

        void highlightReadButton();

        void unHighlightReadButton();

        void showErrorMessage(String message);

        void read(String text);

        boolean isReaderAvailable();

        void showTTSInstallDialog();

        String getCurrentSentence();
    }

    interface Presenter {

        void onViewCreated(String string);

        void onStartOfRead();

        void onEndOfRead();

        void onReadError();

        void onReaderInit(boolean status);

        void readButtonClicked();

        void deactivatedReadButtonClicked();
    }
}
