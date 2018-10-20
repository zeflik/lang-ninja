package pl.jozefniemiec.langninja.ui.sentences.listener.tts;

import android.speech.tts.TextToSpeech;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.ui.sentences.SentenceCardPresenter;

public class OnInitListener implements TextToSpeech.OnInitListener {

    public final SentenceCardPresenter presenter;

    @Inject
    OnInitListener(SentenceCardPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onInit(int status) {
        presenter.onReaderInit(status == TextToSpeech.SUCCESS);
    }
}
