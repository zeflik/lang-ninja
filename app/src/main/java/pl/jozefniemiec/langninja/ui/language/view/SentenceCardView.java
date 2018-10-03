package pl.jozefniemiec.langninja.ui.language.view;

public interface SentenceCardView {

    void showData();

    void showNumbering(String numbering);

    int speak(String text);

    void stopSpeaking();

    void setSpeakerLanguage(String languageCode);

    void showErrorMessage(String message);

    void showSpeakButton();

    void hideSpeakButton();

    void listenSpeech(String languageCode);

    void showSpokenText(String s);

    void showActiveMicrophoneButton();

    void showNormalMicrophoneButton();

    void stopListening();
}
