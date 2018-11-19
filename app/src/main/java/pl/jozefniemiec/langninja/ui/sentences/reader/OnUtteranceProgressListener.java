package pl.jozefniemiec.langninja.ui.sentences.reader;

import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.sentences.reader.ReaderFragmentScope;

@ReaderFragmentScope
public class OnUtteranceProgressListener extends UtteranceProgressListener {

    private static final String TAG = "TTS ProgressListener";
    private final ReaderFragmentContract.Presenter presenter;

    @Inject
    OnUtteranceProgressListener(ReaderFragmentContract.Presenter presenter) {
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
