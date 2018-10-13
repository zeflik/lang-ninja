package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.language.SentenceCardModule;
import pl.jozefniemiec.langninja.ui.language.SentenceCardScope;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCard;
import pl.jozefniemiec.langninja.ui.main.MainActivityModule;
import pl.jozefniemiec.langninja.ui.main.MainActivityScope;
import pl.jozefniemiec.langninja.ui.main.view.MainActivity;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.HomeFragmentProvider;

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, HomeFragmentProvider.class})
    @MainActivityScope
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = SentenceCardModule.class)
    @SentenceCardScope
    abstract SentenceCard bindLanguageCard();

}
