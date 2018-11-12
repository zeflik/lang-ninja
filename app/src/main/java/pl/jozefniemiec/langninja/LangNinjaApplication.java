package pl.jozefniemiec.langninja;

import com.google.firebase.database.FirebaseDatabase;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.jozefniemiec.langninja.di.AppComponent;
import pl.jozefniemiec.langninja.di.DaggerAppComponent;

public class LangNinjaApplication extends DaggerApplication {

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
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
