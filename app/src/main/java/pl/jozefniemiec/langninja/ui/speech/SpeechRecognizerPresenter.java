package pl.jozefniemiec.langninja.ui.speech;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.speech.SpeechRecognizerFragmentScope;

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
    }

    @Override
    public void onSpeechEnded() {
        view.unHighlightSpeechButton();
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
    }

    @Override
    public void deactivatedSpeechButtonClicked() {
        if (view.isSpeechRecognizerAvailable()) {
            view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
        } else {
            view.showSpeechRecognizerInstallDialog();
        }
    }

    @Override
    public void highlightedSpeechButtonClicked() {
        view.stopSpeechListening();
    }

    @Override
    public void speechRecognizerButtonClicked() {
        view.startListening(languageCode);
    }

    @Override
    public void onSpeechRecognizerInit(boolean recognitionAvailable) {
        if (recognitionAvailable) {
            view.findSpeechSupportedLanguages();
        } else {
            view.deactivateSpeechButton();
        }
    }

    @Override
    public void onSpeechSupportedLanguages(List<String> supportedLanguageCodes) {
        if (supportedLanguageCodes.contains(languageCode.replace("_", "-"))) {
            view.activateSpeechButton();
        } else {
            view.deactivateSpeechButton();
        }
    }

    @Override
    public void onViewPause() {
        cancelSpeechListening();
    }

    @Override
    public void onViewCreated(String languageCode) {
        this.languageCode = languageCode;
        view.activateSpeechRecognizer();
    }

    @Override
    public void onSpeechRecognizerInsufficientPermission() {
        view.showSpeechInsufficientPermissionButton();
    }

    @Override
    public void onSpeechPermissionGranted() {
        view.activateSpeechRecognizer();
    }

    @Override
    public void speechRecognizerButtonClickedWithNoPermissions() {
        view.askForSpeechPermissions();
    }

    @Override
    public void speechRecognizerButtonClickedWithNoPermissionsPermanently() {
        view.showInsufficientPermissionDialog();
    }

    private void cancelSpeechListening() {
        if (view.isListeningSpeech()) {
            view.cancelSpeechListening();
            view.unHighlightSpeechButton();
        }
    }
}
