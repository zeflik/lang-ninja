package pl.jozefniemiec.langninja.ui.main.fragment.home.view;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.fragment.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.fragment.home.presenter.HomeFragmentPresenterImpl;

@Module
public abstract class HomeFragmentModule {

    @Binds
    abstract HomeFragmentView provideHomeFragmentView(HomeFragment homeFragment);

    @Binds
    abstract HomeFragmentPresenter provideHomeFragmentPresenter(HomeFragmentPresenterImpl homeFragmentPresenter);
}
