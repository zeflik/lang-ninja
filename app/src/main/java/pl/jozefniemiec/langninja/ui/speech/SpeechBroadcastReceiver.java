package pl.jozefniemiec.langninja.ui.speech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

public class SpeechBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = SpeechBroadcastReceiver.class.getSimpleName();
    private SpeechRecognizerContract.Presenter presenter;

    public SpeechBroadcastReceiver(SpeechRecognizerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle results = getResultExtras(true);
        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            presenter.onSpeechSupportedLanguages(
                    results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
            );
        }
    }
}
