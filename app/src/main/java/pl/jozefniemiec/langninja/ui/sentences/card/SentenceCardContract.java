package pl.jozefniemiec.langninja.ui.sentences.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface SentenceCardContract {

    interface View {

        void showData();

        void showNumbering(int currentPage, int pageCount);

        void activateReadButton();

        void deactivateReadButton();

        void highlightReadButton();

        void unHighlightReadButton();

        boolean setReaderLanguage(Locale locale);

        void read(String sentence);

        boolean isReading();

        void stopReading();

        boolean isSpeechRecognizerAvailable();

        void activateSpeechRecognizer();

        void activateSpeechButton();

        void deactivateSpeechButton();

        void highlightSpeechButton();

        void unHighlightSpeechButton();

        void startListening(String languageCode);

        boolean isListeningSpeech();

        void showCorrectSpokenText(String text);

        void showWrongSpokenText(String text);

        void hideSpokenText();

        void stopSpeechListening();

        void cancelSpeechListening();

        void showErrorMessage(String message);

        void showSpeechRecognizerInstallDialog();

        void showTTSInstallDialog();

        boolean isReaderAvailable();

        void findSpeechSupportedLanguages();
    }

    interface Presenter {

        void loadData(String languageCode);

        void loadPageDataAtPosition(int position, SentencesItemView itemView);

        int getPageCount();

        void pageChanged(int newPosition);

        void readButtonClicked();

        void deactivatedReadButtonClicked();

        void onReaderInit(boolean isWorking);

        void onStartOfRead();

        void onEndOfRead();

        void onReadError();

        void speechRecognizerButtonClicked();

        void highlightedSpeechButtonClicked();

        void deactivatedSpeechButtonClicked();

        void onSpeechRecognizerInit(boolean recognitionAvailable);

        void onReadyForSpeech();

        void onSpeechEnded();

        void onSpeechError(int errorCode);

        void onSpeechResults(ArrayList<String> spokenTextsList);

        void onViewPause();

        void onViewDestroy();

        void onSpeechSupportedLanguages(List<String> stringArrayList);
    }
}
