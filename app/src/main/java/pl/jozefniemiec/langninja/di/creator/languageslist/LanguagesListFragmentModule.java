package pl.jozefniemiec.langninja.di.creator.languageslist;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListContract;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListFragment;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListPresenter;

@Module
abstract class LanguagesListFragmentModule {

    @Binds
    @LanguagesListFragmentScope
    abstract LanguagesListContract.View bindView(LanguagesListFragment fragment);

    @Binds
    @LanguagesListFragmentScope
    abstract LanguagesListContract.Presenter bindPresenter(LanguagesListPresenter presenter);
}
