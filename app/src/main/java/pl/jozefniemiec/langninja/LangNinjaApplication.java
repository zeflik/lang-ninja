package pl.jozefniemiec.langninja;

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
}
