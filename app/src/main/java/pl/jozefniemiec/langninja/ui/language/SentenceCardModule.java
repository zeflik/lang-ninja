package pl.jozefniemiec.langninja.ui.language;

import android.content.Context;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.SentenceRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCard;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;

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
                                 SentenceRepository sentenceRepository) {
        return new SentenceCardPresenterImpl(view, resourcesManager, sentenceRepository);
    }

    @Provides
    static TextToSpeech providesTextToSpeech(Context context, TextToSpeech.OnInitListener listener) {
        return new TextToSpeech(context, listener);
    }

    @Provides
    static SpeechRecognizer providesSpeechRecognizer(Context context) {
        return SpeechRecognizer.createSpeechRecognizer(context);
    }

    @Binds
    abstract SentenceCardView bindLanguageCardView(SentenceCard sentenceCard);

    @Binds
    abstract TextToSpeech.OnInitListener provideTTSOnInitListener(SentenceCard sentenceCard);
}
