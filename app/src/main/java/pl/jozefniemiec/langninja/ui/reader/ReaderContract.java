package pl.jozefniemiec.langninja.ui.reader;

import java.util.Locale;

public interface ReaderContract {

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

        void showProgressBar();

        void hideProgressBar();
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
