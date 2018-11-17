package pl.jozefniemiec.langninja.di.creator;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreatorContract;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreatorPresenter;

@Module
public abstract class SentenceCreatorModule {

    @Binds
    @SentenceCreatorScope
    abstract SentenceCreatorContract.Presenter bindPresenter(SentenceCreatorPresenter presenter);

    @Binds
    @SentenceCreatorScope
    abstract SentenceCreatorContract.View bindView(SentenceCreator view);
}


