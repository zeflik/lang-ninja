package pl.jozefniemiec.langninja.ui.main.fragment.home.view;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HomeFragmentModule {

    @Binds
    abstract HomeFragmentView provideHomeFragmentView(HomeFragment homeFragment);
}
