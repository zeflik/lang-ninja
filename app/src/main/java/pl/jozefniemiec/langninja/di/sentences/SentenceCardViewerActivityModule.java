package pl.jozefniemiec.langninja.di.sentences;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerContract;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerPresenter;

@Module
public abstract class SentenceCardViewerActivityModule {

    @Binds
    @SentenceCardViewerActivityScope
    abstract SentenceCardViewerContract.View bindView(SentenceCardViewerActivity activity);

    @Binds
    @SentenceCardViewerActivityScope
    abstract SentenceCardViewerContract.Presenter bindPresenter(SentenceCardViewerPresenter presenter);
}
