package pl.jozefniemiec.langninja.ui.main.di;

import android.support.v4.app.FragmentManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.main.MainPresenter;
import pl.jozefniemiec.langninja.ui.main.MainPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.MainView;

@Module
public abstract class MainActivityModule {

    @Provides
    @MainActivityScope
    static FragmentManager provideFragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }

    @Provides
    @MainActivityScope
    static MainPresenter provideHomePresenter(MainView mainView) {
        return new MainPresenterImpl(mainView);
    }

    @Binds
    @MainActivityScope
    abstract MainView provideMainView(MainActivity mainActivity);
}
