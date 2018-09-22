package pl.jozefniemiec.langninja.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.resources.ResourcesManager;

@Module
public abstract class AppModule {

    @Provides
    static LanguageRepository provideLanguageRepository(Context context) {
        return new RoomLanguageRepository(context);
    }

    @Provides
    static Resources provideResources(Application application) {
        return application.getResources();
    }

    @Provides
    static ResourcesManager provideResourcesManager(Resources resources) {
        return new AndroidResourceManager(resources);
    }

    @Binds
    abstract Context provideContext(Application application);
}
