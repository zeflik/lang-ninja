package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorModule;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.di.creator.languageslist.LanguagesListFragmentProvider;
import pl.jozefniemiec.langninja.di.main.MainActivityModule;
import pl.jozefniemiec.langninja.di.main.MainActivityScope;
import pl.jozefniemiec.langninja.di.main.languages.LanguagesFragmentProvider;
import pl.jozefniemiec.langninja.di.sentences.SentenceCardModule;
import pl.jozefniemiec.langninja.di.sentences.SentenceCardScope;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, LanguagesFragmentProvider.class})
    @MainActivityScope
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = SentenceCardModule.class)
    @SentenceCardScope
    abstract SentenceCard bindLanguageCard();

    @ContributesAndroidInjector(modules = {
            SentenceCreatorModule.class,
            LanguagesFragmentProvider.class,
            LanguagesListFragmentProvider.class})
    @SentenceCreatorScope
    abstract SentenceCreator bindSentenceCreator();
}
