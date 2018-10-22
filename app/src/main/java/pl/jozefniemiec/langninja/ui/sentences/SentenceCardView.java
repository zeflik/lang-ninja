package pl.jozefniemiec.langninja.ui.sentences;

import java.util.Locale;

public interface SentenceCardView {

    void showData();

    void showNumbering(String numbering);

    void activateReadButton();

    void deactivateReadButton();

    void highlightReadButton();

    void unHighlightReadButton();

    boolean setReaderLanguage(Locale locale);

    void read(String sentence);

    boolean isReading();

    void stopReading();

    void activateSpeechButton();

    void deactivateSpeechButton();

    void highlightSpeechButton();

    void unHighlightSpeechButton();

    void startListening(String languageCode);

    boolean isListeningSpeech();

    void showSpokenText(String s);

    void stopSpeechListening();

    void cancelSpeechListening();

    void showErrorMessage(String message);

    void showSpeechRecognizerInstallDialog();

    void showTTSInstallDialog();

    boolean isSpeechRecognizerAvailable();
}
