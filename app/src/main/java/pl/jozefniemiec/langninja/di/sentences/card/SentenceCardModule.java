package pl.jozefniemiec.langninja.di.sentences.card;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardContract;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardFragment;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.sentences.card.SentencesPageAdapter;

@Module
public abstract class SentenceCardModule {

    @Provides
    @SentenceCardScope
    static SentencesPageAdapter
    provideLanguagePageAdapter(SentenceCardFragment activity, SentenceCardContract.Presenter presenter) {
        return new SentencesPageAdapter(activity.requireContext(), presenter);
    }

    @Provides
    @SentenceCardScope
    static SentenceCardContract.Presenter
    provideLanguageCardPresenter(SentenceCardContract.View view,
                                 ResourcesManager resourcesManager,
                                 SentenceRepository sentenceRepository) {
        return new SentenceCardPresenter(view, resourcesManager, sentenceRepository);
    }

    @Binds
    @SentenceCardScope
    abstract SentenceCardContract.View bindLanguageCardView(SentenceCardFragment sentenceCard);
}
