package pl.jozefniemiec.langninja;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.jozefniemiec.langninja.di.AppComponent;
import pl.jozefniemiec.langninja.di.DaggerAppComponent;
import pl.jozefniemiec.langninja.service.LocalDatabaseManager;

public class LangNinjaApplication extends DaggerApplication {

    public static String APP_PACKAGE_NAME;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APP_PACKAGE_NAME = this.getPackageName();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference sentencesReference = firebaseDatabase.getReference("data");
        new LocalDatabaseManager(this).syncData(sentencesReference);
    }
}
