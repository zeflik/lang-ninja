package pl.jozefniemiec.langninja.ui.sentences.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class SpeechRecognitionListener implements RecognitionListener {

    private final SpeechRecognizerContract.Presenter presenter;
    private boolean listeningSpeech;

    @Inject
    public SpeechRecognitionListener(SpeechRecognizerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        listeningSpeech = true;
        presenter.onReadyForSpeech();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        listeningSpeech = false;
        presenter.onSpeechEnded();
    }

    @Override
    public void onError(int errorCode) {
        listeningSpeech = false;
        presenter.onSpeechError(errorCode);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> spokenTextsList = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
        presenter.onSpeechResults(spokenTextsList);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public boolean isListeningSpeech() {
        return listeningSpeech;
    }
}
