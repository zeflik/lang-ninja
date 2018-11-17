package pl.jozefniemiec.langninja.ui.sentences.tts;

import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.ui.sentences.SentenceCardContract;

public class OnUtteranceProgressListener extends UtteranceProgressListener {

    private static final String TAG = "TTS ProgressListener";

    public final SentenceCardContract.Presenter presenter;

    @Inject
    OnUtteranceProgressListener(SentenceCardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart(String utteranceId) {
        Log.d(TAG, "Reading: " + utteranceId);
        presenter.onStartOfRead();
    }

    @Override
    public void onDone(String utteranceId) {
        Log.d(TAG, "End of reading:" + utteranceId);
        presenter.onEndOfRead();
    }

    @Override
    public void onError(String utteranceId) {
        presenter.onReadError();
        Log.d(TAG, "Error:" + utteranceId);
    }
}
