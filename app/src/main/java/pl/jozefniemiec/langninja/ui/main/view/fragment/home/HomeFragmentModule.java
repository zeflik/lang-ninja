package pl.jozefniemiec.langninja.ui.main.view.fragment.home;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragmentView;

@Module
public abstract class HomeFragmentModule {

    @Binds
    abstract HomeFragmentView provideHomeFragmentView(HomeFragment homeFragment);

    @Binds
    abstract HomeFragmentPresenter provideHomeFragmentPresenter(HomeFragmentPresenterImpl homeFragmentPresenter);
}
