package pl.jozefniemiec.langninja.ui.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragment;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragmentModule;

@Module
public abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = HomeFragmentModule.class)
    abstract HomeFragment provideHomeFragment();
}
