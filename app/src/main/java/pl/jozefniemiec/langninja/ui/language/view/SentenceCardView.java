package pl.jozefniemiec.langninja.ui.language.view;

public interface SentenceCardView {

    void showData();

    void showNumbering(String numbering);

    void showReadButton();

    void hideReadButton();

    void setReaderLanguage(String languageCode);

    int read(String text);

    void stopReading();

    void activateSpeechButton();

    void highlightSpeakButton();

    void unHighlightSpeakButton();

    void listenSpeech(String languageCode);

    void showSpokenText(String s);

    void stopSpeechListening();

    void cancelSpeechListening();

    void showErrorMessage(String message);

    void deactivateSpeechButton();
}
