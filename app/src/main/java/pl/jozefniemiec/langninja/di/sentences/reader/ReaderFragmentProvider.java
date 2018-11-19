package pl.jozefniemiec.langninja.di.sentences.reader;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.sentences.reader.ReaderFragment;

@Module
public abstract class ReaderFragmentProvider {

    @ContributesAndroidInjector(modules = ReaderFragmentModule.class)
    @ReaderFragmentScope
    abstract ReaderFragment bindReaderFragment();
}
