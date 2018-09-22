package pl.jozefniemiec.langninja.ui.main;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;


@Module
public abstract class MainActivityModule {

    @Provides
    static LanguageRepository provideLanguageRepository(Context context) {
        return new RoomLanguageRepository(context);
    }

    @Provides
    static FragmentManager provideFragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }

    @Binds
    abstract MainView provideMainView(MainActivity mainActivity);
}
