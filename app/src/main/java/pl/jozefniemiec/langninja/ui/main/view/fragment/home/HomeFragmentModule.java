package pl.jozefniemiec.langninja.ui.main.view.fragment.home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter.HomeFragmentPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragmentView;
import pl.jozefniemiec.langninja.utils.Utility;

@Module
public abstract class HomeFragmentModule {

    @Provides
    static GridLayoutManager provideGridLayoutManager(Context context) {
        int numberOfColumns = Utility.calculateNoOfColumns(context);
        return new GridLayoutManager(context, numberOfColumns);
    }

    @Binds
    abstract HomeFragmentView provideHomeFragmentView(HomeFragment homeFragment);

    @Binds
    abstract HomeFragmentPresenter provideHomeFragmentPresenter(HomeFragmentPresenterImpl homeFragmentPresenter);
}
