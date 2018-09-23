package pl.jozefniemiec.langninja.ui.language;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.SentenceRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenter;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCard;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;

@Module
public abstract class SentenceCardModule {

    @Provides
    @SentenceCardScope
    static SentencesPageAdapter
    provideLanguagePageAdapter(SentenceCard activity, LanguageCardPresenter presenter) {
        return new SentencesPageAdapter(activity, presenter);
    }

    @Provides
    @SentenceCardScope
    static LanguageCardPresenter
    provideLanguageCardPresenter(SentenceCardView view,
                                 ResourcesManager resourcesManager,
                                 SentenceRepository sentenceRepository) {
        return new LanguageCardPresenterImpl(view, resourcesManager, sentenceRepository);
    }

    @Binds
    abstract SentenceCardView bindLanguageCardView(SentenceCard sentenceCard);
}
