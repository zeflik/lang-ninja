package pl.jozefniemiec.langninja.ui.main.view.fragment.home;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment;

@Module
public abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = HomeFragmentModule.class)
    abstract HomeFragment provideHomeFragment();
}
