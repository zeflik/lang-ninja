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

        void showErrorMessage(String message);

        void editSentence(String bla);

        void notifyDataChanged();

        void close();

        void showEditButtons();

        void hideEditButtons();
    }

    interface Presenter {

        void onMenuCreated(String sentenceId);

        void onTextSpokenResult(List<String> spokenTextsList);

        void onSentencePageChanged(int position, int pageCount);

        void onSentenceEditButtonClicked(String sentenceId);

        void onSentenceRemoveButtonClicked(String sentenceId);
    }
}
