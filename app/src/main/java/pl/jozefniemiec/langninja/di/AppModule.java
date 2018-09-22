package pl.jozefniemiec.langninja.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;

@Module
public abstract class AppModule {

    @Provides
    static LanguageRepository provideLanguageRepository(Context context) {
        return new RoomLanguageRepository(context);
    }

    @Binds
    abstract Context provideContext(Application application);
}
