package pl.jozefniemiec.langninja.ui.sentences.di;

import android.content.Context;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.service.ApplicationsManager;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardView;
import pl.jozefniemiec.langninja.ui.sentences.SentencesPageAdapter;
import pl.jozefniemiec.langninja.ui.sentences.listener.tts.OnInitListener;
import pl.jozefniemiec.langninja.ui.sentences.listener.tts.OnUtteranceProgressListener;

@Module
public abstract class SentenceCardModule {

    @Provides
    @SentenceCardScope
    static SentencesPageAdapter
    provideLanguagePageAdapter(SentenceCard activity, SentenceCardPresenter presenter) {
        return new SentencesPageAdapter(activity, presenter);
    }

    @Provides
    @SentenceCardScope
    static SentenceCardPresenter
    provideLanguageCardPresenter(SentenceCardView view,
                                 ResourcesManager resourcesManager,
                                 SentenceRepository sentenceRepository,
                                 ApplicationsManager applicationsManager) {
        return new SentenceCardPresenterImpl(view, resourcesManager, sentenceRepository, applicationsManager);
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
    abstract SentenceCardView bindLanguageCardView(SentenceCard sentenceCard);

    @Binds
    @SentenceCardScope
    abstract TextToSpeech.OnInitListener bindTextToSpeechOnInitListener(OnInitListener listener);

    @Binds
    @SentenceCardScope
    abstract UtteranceProgressListener bindUtteranceProgressListener(OnUtteranceProgressListener listener);

}
