package pl.jozefniemiec.langninja.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;

@Module(includes = ContextModule.class)
public class LanguageRepositoryModule {

    @Provides
    LanguageRepository languageRepository(Context context) {
        return new RoomLanguageRepository(context);
    }
}
