package pl.jozefniemiec.langninja.di.reader;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.reader.OnInitListener;
import pl.jozefniemiec.langninja.ui.reader.OnUtteranceProgressListener;
import pl.jozefniemiec.langninja.ui.reader.ReaderContract;
import pl.jozefniemiec.langninja.ui.reader.ReaderFragment;
import pl.jozefniemiec.langninja.ui.reader.ReaderPresenter;

@Module
abstract class ReaderFragmentModule {

    @Provides
    @ReaderFragmentScope
    static TextToSpeech providesTextToSpeech(Context context, TextToSpeech.OnInitListener listener) {
        return new TextToSpeech(context, listener);
    }

    @Binds
    @ReaderFragmentScope
    abstract ReaderContract.View bindView(ReaderFragment fragment);

    @Binds
    @ReaderFragmentScope
    abstract ReaderContract.Presenter bindPresenter(ReaderPresenter presenter);

    @Binds
    @ReaderFragmentScope
    abstract TextToSpeech.OnInitListener bindTextToSpeechOnInitListener(OnInitListener listener);

    @Binds
    @ReaderFragmentScope
    abstract UtteranceProgressListener bindUtteranceProgressListener(OnUtteranceProgressListener listener);
}
