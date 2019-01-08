package pl.jozefniemiec.langninja.ui.sentences;

import java.util.List;

public interface SentenceCardViewerContract {

    interface View {

        String getCurrentSentence();

        void showCorrectSpokenText(String text);

        void showWrongSpokenText(String text);

        void hideSpokenText();

        void stopReading();

        void stopListening();

        void updateNumbering(int position, int pageCount);
    }

    interface Presenter {

        void onViewCreated();

        void onTextSpokenResult(List<String> spokenTextsList);

        void onSentencePageChanged(int position, int pageCount);
    }
}
