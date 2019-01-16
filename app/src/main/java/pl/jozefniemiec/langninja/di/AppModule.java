package pl.jozefniemiec.langninja.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.BuildConfig;
import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.UserRepositoryImpl;
import pl.jozefniemiec.langninja.data.repository.firebase.UserSentenceRepositoryImpl;
import pl.jozefniemiec.langninja.data.repository.room.RoomLanguageRepository;
import pl.jozefniemiec.langninja.data.repository.room.RoomSentenceRepository;
import pl.jozefniemiec.langninja.data.resources.AndroidResourceManager;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.service.FirebaseAuthService;
import pl.jozefniemiec.langninja.storage.ImagesStorage;
import pl.jozefniemiec.langninja.storage.ImagesStorageImpl;

@Module
abstract class AppModule {

    @Provides
    static Resources provideResources(Application application) {
        return application.getResources();
    }

    @Provides
    @Singleton
    static OkHttp3Downloader provideOkHttp3Downloader(Context context) {
        return new OkHttp3Downloader(context, Integer.MAX_VALUE);
    }

    @Provides
    @Singleton
    static Picasso providesPicasso(Context context, OkHttp3Downloader okHttp3Downloader) {
        Picasso build = new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();
        if (BuildConfig.DEBUG) {
            build.setIndicatorsEnabled(true);
            //  build.setLoggingEnabled(true);
        }
        return build;
    }

    @Provides
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Binds
    abstract ResourcesManager bindResourcesManager(AndroidResourceManager androidResourceManager);

    @Binds
    abstract LanguageRepository bindLanguageRepository(RoomLanguageRepository repository);

    @Binds
    abstract SentenceRepository bindSentenceRepository(RoomSentenceRepository repository);

    @Binds
    abstract UserSentenceRepository bindSentenceCandidateRepository(UserSentenceRepositoryImpl db);

    @Binds
    abstract AuthService bindAuthService(FirebaseAuthService authService);

    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract UserRepository bindUserRepository(UserRepositoryImpl userRepository);

    @Binds
    abstract ImagesStorage bindImagesStorage(ImagesStorageImpl storage);
}
