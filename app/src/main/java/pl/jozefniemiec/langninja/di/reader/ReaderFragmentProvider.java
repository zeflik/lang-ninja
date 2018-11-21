package pl.jozefniemiec.langninja.di.reader;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.reader.ReaderFragment;

@Module
public abstract class ReaderFragmentProvider {

    @ContributesAndroidInjector(modules = ReaderFragmentModule.class)
    @ReaderFragmentScope
    abstract ReaderFragment bindReaderFragment();
}
