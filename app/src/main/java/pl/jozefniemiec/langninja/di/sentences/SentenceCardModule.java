package pl.jozefniemiec.langninja.di.sentences;

import android.content.Context;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardContract;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.sentences.SentencesPageAdapter;
import pl.jozefniemiec.langninja.ui.sentences.tts.OnInitListener;
import pl.jozefniemiec.langninja.ui.sentences.tts.OnUtteranceProgressListener;

@Module
public abstract class SentenceCardModule {

    @Provides
    @SentenceCardScope
    static SentencesPageAdapter
    provideLanguagePageAdapter(SentenceCard activity, SentenceCardContract.Presenter presenter) {
        return new SentencesPageAdapter(activity, presenter);
    }

    @Provides
    @SentenceCardScope
    static SentenceCardContract.Presenter
    provideLanguageCardPresenter(SentenceCardContract.View view,
                                 ResourcesManager resourcesManager,
                                 SentenceRepository sentenceRepository) {
        return new SentenceCardPresenterImpl(view, resourcesManager, sentenceRepository);
    }

    @Provides
    @SentenceCardScope
    static SpeechRecognizer providesSpeechRecognizer(Context context) {
        return SpeechRecognizer.createSpeechRecognizer(context);
    }

    @Provides
    @SentenceCardScope
    static TextToSpeech providesTextToSpeech(Context context, TextToSpeech.OnInitListener listener) {
        return new TextToSpeech(context, listener);
    }

    @Binds
    @SentenceCardScope
    abstract SentenceCardContract.View bindLanguageCardView(SentenceCard sentenceCard);

    @Binds
    @SentenceCardScope
    abstract TextToSpeech.OnInitListener bindTextToSpeechOnInitListener(OnInitListener listener);

    @Binds
    @SentenceCardScope
    abstract UtteranceProgressListener bindUtteranceProgressListener(OnUtteranceProgressListener listener);

}
