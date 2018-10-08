package pl.jozefniemiec.langninja.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;


public class ReaderImpl implements Reader, TextToSpeech.OnInitListener {

    private static final String TAG = "Reader";

    private final TextToSpeech textToSpeech;
    private ReaderListener listener;

    @Inject
    ReaderImpl(Context context) {
        this.textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "Initialized and working");
            listener.onReaderInit(true);
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                    Log.d(TAG, "End of reading:" + utteranceId);
                    listener.onEndOfRead();
                }

                @Override
                public void onError(String utteranceId) {
                    Log.d(TAG, "Error while reading: " + utteranceId);
                    listener.onReadError();
                }

                @Override
                public void onStart(String utteranceId) {
                    Log.d(TAG, "Reading: " + utteranceId);
                    listener.onStartOfRead();
                }
            });
        } else {
            Log.d(TAG, "Reader initialized, but not working!");
            listener.onReaderInit(false);
        }
    }

    @Override
    public void read(String text) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
        Log.d(TAG, "Speak result: " + result);
    }

    @Override
    public boolean setLanguage(String string) {
        Locale locale = new Locale(string);
        return textToSpeech.setLanguage(locale) == TextToSpeech.LANG_AVAILABLE;
    }

    @Override
    public void stop() {
        textToSpeech.stop();
    }

    @Override
    public void shutdown() {
        textToSpeech.shutdown();
    }

    @Override
    public void setOnReadListener(ReaderListener listener) {
        this.listener = listener;
    }
}
