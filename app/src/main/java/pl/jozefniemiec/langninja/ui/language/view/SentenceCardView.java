package pl.jozefniemiec.langninja.ui.language.view;

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

    void stopReading();

    void activateSpeechRecognizer();

    void activateSpeechButton();

    void deactivateSpeechButton();

    void highlightSpeechButton();

    void unHighlightSpeechButton();

    void startListening(String languageCode);

    void showSpokenText(String s);

    void stopSpeechListening();

    void cancelSpeechListening();

    void showErrorMessage(String message);
}
