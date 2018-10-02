package pl.jozefniemiec.langninja.ui.language.view;

public interface SentenceCardView {

    void showData();

    void showNumbering(String numbering);

    int speak(String text);

    void stopSpeaking();

    void setReaderLanguage(String languageCode);

    void showErrorMessage(String message);

    void showPlayButton();

    void hidePlayButton();

    void speechListen(String languageCode);

    void showSpokenText(String s);
}
