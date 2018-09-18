package pl.jozefniemiec.langninja.di;

import dagger.Component;
import pl.jozefniemiec.langninja.activities.main.MainActivity;

@Component(modules = {LanguageRepositoryModule.class, FragmentManagerModule.class})
public interface LangNinjaApplicationComponent {

    void injectHomeActivity(MainActivity mainActivity);
}
