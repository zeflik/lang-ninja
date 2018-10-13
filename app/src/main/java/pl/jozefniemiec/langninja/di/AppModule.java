package pl.jozefniemiec.langninja.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.data.repository.RoomSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;

@Module
abstract class AppModule {

    @Provides
    static Resources provideResources(Application application) {
        return application.getResources();
    }

    @Binds
    abstract ResourcesManager bindResourcesManager(AndroidResourceManager androidResourceManager);

    @Binds
    abstract LanguageRepository bindLanguageRepository(RoomLanguageRepository repository);

    @Binds
    abstract SentenceRepository bindSentenceRepository(RoomSentenceRepository repository);

    @Binds
    abstract Context bindContext(Application application);
}
