package pl.jozefniemiec.langninja.di.main.languages;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment;

@Module
public abstract class LanguagesFragmentProvider {

    @ContributesAndroidInjector(modules = LanguagesFragmentModule.class)
    @LanguagesFragmentScope
    abstract LanguagesFragment provideHomeFragment();
}
