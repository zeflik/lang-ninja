package pl.jozefniemiec.langninja.di.sentences.speech;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognizerFragment;

@Module
public abstract class SpeechRecognizerFragmentProvider {

    @ContributesAndroidInjector(modules = SpeechRecognizerFragmentModule.class)
    @SpeechRecognizerFragmentScope
    abstract SpeechRecognizerFragment bindSpeechRecognizerFragment();
}
