package pl.jozefniemiec.langninja.di.sentences.speech;

import android.content.Context;
import android.speech.SpeechRecognizer;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognizerContract;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognizerFragment;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognizerPresenter;

@Module
abstract class SpeechRecognizerFragmentModule {

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
