package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.language.LanguageCardModule;
import pl.jozefniemiec.langninja.ui.language.LanguageCardScope;
import pl.jozefniemiec.langninja.ui.language.view.LanguageCard;
import pl.jozefniemiec.langninja.ui.main.MainActivityModule;
import pl.jozefniemiec.langninja.ui.main.view.MainActivity;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.HomeFragmentProvider;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, HomeFragmentProvider.class})
    @MainActivityScope
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = LanguageCardModule.class)
    @LanguageCardScope
    abstract LanguageCard bindLanguageCard();

}
