package pl.jozefniemiec.langninja.ui.sentences.speech;

import java.util.ArrayList;
import java.util.List;

public interface SpeechRecognizerContract {

    interface View {

        void activateSpeechButton();

        void deactivateSpeechButton();

        void highlightSpeechButton();

        void unHighlightSpeechButton();

        void startListening(String languageCode);

        boolean isListeningSpeech();

        void stopSpeechListening();

        void cancelSpeechListening();

        void showSpeechRecognizerInstallDialog();

        boolean isSpeechRecognizerAvailable();

        void findSpeechSupportedLanguages();

        void showErrorMessage(String message);

        void onSpeechResult(ArrayList<String> spokenTextsList);
    }

    interface Presenter {

        void onReadyForSpeech();

        void onSpeechEnded();

        void onSpeechResults(ArrayList<String> spokenTextsList);

        void onSpeechError(int errorCode);

        void deactivatedSpeechButtonClicked();

        void highlightedSpeechButtonClicked();

        void speechRecognizerButtonClicked();

        void onSpeechRecognizerInit(boolean recognitionAvailable);

        void onSpeechSupportedLanguages(List<String> supportedLanguageCodes);

        void onViewPause();

        void onViewCreated(String languageCode);
    }
}
