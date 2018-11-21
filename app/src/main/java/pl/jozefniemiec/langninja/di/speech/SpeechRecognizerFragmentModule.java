package pl.jozefniemiec.langninja.di.speech;

import android.content.Context;
import android.speech.SpeechRecognizer;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerContract;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerFragment;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerPresenter;

@Module
public abstract class SpeechRecognizerFragmentModule {

    @Binds
    @SpeechRecognizerFragmentScope
    abstract SpeechRecognizerContract.View bindView(SpeechRecognizerFragment fragment);

    @Binds
    @SpeechRecognizerFragmentScope
    abstract SpeechRecognizerContract.Presenter bindPresenter(SpeechRecognizerPresenter presenter);

    @Provides
    @SpeechRecognizerFragmentScope
    static SpeechRecognizer providesSpeechRecognizer(Context context) {
        return SpeechRecognizer.createSpeechRecognizer(context);
    }
}
