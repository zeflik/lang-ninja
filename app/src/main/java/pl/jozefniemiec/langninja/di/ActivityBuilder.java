package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.main.di.MainActivityModule;
import pl.jozefniemiec.langninja.ui.main.di.MainActivityScope;
import pl.jozefniemiec.langninja.ui.main.languages.di.LanguagesFragmentProvider;
import pl.jozefniemiec.langninja.ui.main.send.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.main.send.creator.di.SentenceCreatorModule;
import pl.jozefniemiec.langninja.ui.main.send.creator.di.SentenceCreatorScope;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCard;
import pl.jozefniemiec.langninja.ui.sentences.di.SentenceCardModule;
import pl.jozefniemiec.langninja.ui.sentences.di.SentenceCardScope;

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, LanguagesFragmentProvider.class})
    @MainActivityScope
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = SentenceCardModule.class)
    @SentenceCardScope
    abstract SentenceCard bindLanguageCard();

    @ContributesAndroidInjector(modules = SentenceCreatorModule.class)
    @SentenceCreatorScope
    abstract SentenceCreator bindSentenceCreator();
}
