package pl.jozefniemiec.langninja.ui.reader;

import android.speech.tts.TextToSpeech;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.reader.ReaderFragmentScope;

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
