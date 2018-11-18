package pl.jozefniemiec.langninja.di.sentences.card;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCard;

@Module
public abstract class SentenceCardProvider {

    @ContributesAndroidInjector(modules = SentenceCardModule.class)
    @SentenceCardScope
    abstract SentenceCard bindLanguageCard();
}
