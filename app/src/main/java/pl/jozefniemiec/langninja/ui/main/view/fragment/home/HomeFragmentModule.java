package pl.jozefniemiec.langninja.ui.main.view.fragment.home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

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
    @HomeFragmentScope
    static GridLayoutManager provideGridLayoutManager(Context context) {
        int numberOfColumns = Utility.calculateNoOfColumns(context);
        return new GridLayoutManager(context, numberOfColumns);
    }

    @Binds
    @HomeFragmentScope
    abstract View.OnClickListener provideOnClickListener(HomeFragment homeFragment);

    @Binds
    @HomeFragmentScope
    abstract HomeFragmentPresenter
    provideHomeFragmentPresenter(HomeFragmentPresenterImpl presenter);

    @Binds
    @HomeFragmentScope
    abstract HomeFragmentView provideHomeFragmentView(HomeFragment homeFragment);
}
