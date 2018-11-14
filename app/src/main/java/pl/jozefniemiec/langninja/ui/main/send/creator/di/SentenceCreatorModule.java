package pl.jozefniemiec.langninja.ui.main.send.creator.di;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.send.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.main.send.creator.SentenceCreatorContract;
import pl.jozefniemiec.langninja.ui.main.send.creator.SentenceCreatorPresenter;

@Module
public abstract class SentenceCreatorModule {

    @Binds
    @SentenceCreatorScope
    abstract SentenceCreatorContract.Presenter bindPresenter(SentenceCreatorPresenter presenter);

    @Binds
    @SentenceCreatorScope
    abstract SentenceCreatorContract.View bindView(SentenceCreator view);
}


