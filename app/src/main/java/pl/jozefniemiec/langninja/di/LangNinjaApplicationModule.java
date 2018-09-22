package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.activities.main.MainActivity;

@Module
public abstract class LangNinjaApplicationModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}
