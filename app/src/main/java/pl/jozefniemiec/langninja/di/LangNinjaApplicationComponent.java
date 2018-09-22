package pl.jozefniemiec.langninja.di;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import pl.jozefniemiec.langninja.LangNinjaApplication;

@Component(modules = {AndroidInjectionModule.class, LangNinjaApplicationModule.class})
public interface LangNinjaApplicationComponent extends AndroidInjector<LangNinjaApplication> {

}
