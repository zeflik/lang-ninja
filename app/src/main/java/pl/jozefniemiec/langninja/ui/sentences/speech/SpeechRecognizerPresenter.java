package pl.jozefniemiec.langninja.ui.sentences.speech;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.sentences.speech.SpeechRecognizerFragmentScope;

@SpeechRecognizerFragmentScope
public class SpeechRecognizerPresenter implements SpeechRecognizerContract.Presenter {

    private static final String TAG = SpeechRecognizerContract.class.getSimpleName();
    private final SpeechRecognizerContract.View view;
    private final ResourcesManager resourcesManager;
    private String languageCode;

    @Inject
    SpeechRecognizerPresenter(SpeechRecognizerContract.View view, ResourcesManager resourcesManager) {
        this.view = view;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void onReadyForSpeech() {
        view.highlightSpeechButton();
        Log.d(TAG, "onReadyForSpeech: ");
    }

    @Override
    public void onSpeechEnded() {
        view.unHighlightSpeechButton();
        Log.d(TAG, "onSpeechEnded: ");
    }

    @Override
    public void onSpeechResults(ArrayList<String> spokenTextsList) {
        view.onSpeechResult(spokenTextsList);
    }

    @Override
    public void onSpeechError(int errorCode) {
        view.unHighlightSpeechButton();
        String message = resourcesManager.findOnSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
        Log.d(TAG, "onSpeechError: " + message);
    }

    @Override
    public void deactivatedSpeechButtonClicked() {
        if (view.isSpeechRecognizerAvailable()) {
            view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
            Log.d(TAG, "deactivatedSpeechButtonClicked: activating Speech");
        } else {
            view.showSpeechRecognizerInstallDialog();
            Log.d(TAG, "deactivatedSpeechButtonClicked: installing");
        }
    }

    @Override
    public void highlightedSpeechButtonClicked() {
        view.stopSpeechListening();
        Log.d(TAG, "highlightedSpeechButtonClicked: ");
    }

    @Override
    public void speechRecognizerButtonClicked() {
        view.startListening(languageCode);
        Log.d(TAG, "speechRecognizerButtonClicked: start listening " + languageCode);
    }

    @Override
    public void onSpeechRecognizerInit(boolean recognitionAvailable) {
        if (recognitionAvailable) {
            view.findSpeechSupportedLanguages();
        } else {
            view.deactivateSpeechButton();
        }
        Log.d(TAG, "onSpeechRecognizerInit: recognition available: " + recognitionAvailable);
    }

    @Override
    public void onSpeechSupportedLanguages(List<String> supportedLanguageCodes) {
        if (supportedLanguageCodes.contains(languageCode.replace("_", "-"))) {
            view.activateSpeechButton();
        } else {
            view.deactivateSpeechButton();
        }
        Log.d(TAG, "onSpeechSupportedLanguages: ");
    }

    @Override
    public void onViewPause() {
        cancelSpeechListening();
    }

    @Override
    public void onViewCreated(String languageCode) {
        this.languageCode = languageCode;
    }

    private void cancelSpeechListening() {
        if (view.isListeningSpeech()) {
            view.cancelSpeechListening();
            view.unHighlightSpeechButton();
        }
    }
}
