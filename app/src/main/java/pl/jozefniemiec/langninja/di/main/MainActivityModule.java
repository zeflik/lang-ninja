package pl.jozefniemiec.langninja.di.main;

import android.support.v4.app.FragmentManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.main.MainContract;
import pl.jozefniemiec.langninja.ui.main.MainPresenterImpl;

@Module
public abstract class MainActivityModule {

    @Provides
    @MainActivityScope
    static FragmentManager provideFragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }

    @Provides
    @MainActivityScope
    static MainContract.Presenter provideHomePresenter(MainContract.View mainView) {
        return new MainPresenterImpl(mainView);
    }

    @Binds
    @MainActivityScope
    abstract MainContract.View provideMainView(MainActivity mainActivity);
}
