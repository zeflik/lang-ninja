package pl.jozefniemiec.langninja.ui.sentences.reader;

import android.speech.tts.TextToSpeech;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.sentences.reader.ReaderFragmentScope;

@ReaderFragmentScope
public class OnInitListener implements TextToSpeech.OnInitListener {

    private final ReaderContract.Presenter presenter;

    @Inject
    OnInitListener(ReaderContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onInit(int status) {
        presenter.onReaderInit(status == TextToSpeech.SUCCESS);
    }
}
