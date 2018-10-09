package pl.jozefniemiec.langninja.ui.language.view.listener.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;

import java.util.ArrayList;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;

public class SpeechRecognitionListener implements RecognitionListener {

    private final SentenceCardPresenter presenter;

    @Inject
    public SpeechRecognitionListener(SentenceCardPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
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
        presenter.onSpeechEnded();
    }

    @Override
    public void onError(int errorCode) {
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
}
