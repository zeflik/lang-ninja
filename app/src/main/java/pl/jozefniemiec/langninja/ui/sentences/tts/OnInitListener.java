package pl.jozefniemiec.langninja.ui.sentences.tts;

import android.speech.tts.TextToSpeech;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.ui.sentences.SentenceCardContract;


public class OnInitListener implements TextToSpeech.OnInitListener {

    public final SentenceCardContract.Presenter presenter;

    @Inject
    OnInitListener(SentenceCardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onInit(int status) {
        presenter.onReaderInit(status == TextToSpeech.SUCCESS);
    }
}
