package pl.jozefniemiec.langninja.di.creator.languageslist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListFragment;

@Module
public abstract class LanguagesListFragmentProvider {

    @ContributesAndroidInjector(modules = LanguagesListFragmentModule.class)
    @LanguagesListFragmentScope
    abstract LanguagesListFragment provideHomeFragment();
}
