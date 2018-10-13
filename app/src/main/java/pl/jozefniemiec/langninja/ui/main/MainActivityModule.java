package pl.jozefniemiec.langninja.ui.main;

import android.support.v4.app.FragmentManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.ui.main.presenter.MainPresenter;
import pl.jozefniemiec.langninja.ui.main.presenter.MainPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.view.MainActivity;
import pl.jozefniemiec.langninja.ui.main.view.MainView;

@Module
public abstract class MainActivityModule {

    @Provides
    @MainActivityScope
    static FragmentManager provideFragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }

    @Provides
    @MainActivityScope
    static MainPresenter provideHomePresenter(MainView mainView, LanguageRepository languageRepository) {
        return new MainPresenterImpl(mainView, languageRepository);
    }

    @Binds
    @MainActivityScope
    abstract MainView provideMainView(MainActivity mainActivity);
}
