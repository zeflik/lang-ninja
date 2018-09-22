package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.main.MainActivityModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

}
