package pl.jozefniemiec.langninja.ui.language;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenter;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.language.view.LanguageCard;
import pl.jozefniemiec.langninja.ui.language.view.LanguageCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.LanguagePageAdapter;

@Module
public abstract class LanguageCardModule {

    @Provides
    @LanguageCardScope
    static LanguagePageAdapter providesLanguagePageAdapter(LanguageCard activity) {
        return new LanguagePageAdapter(activity);
    }

    @Provides
    @LanguageCardScope
    static LanguageCardPresenter bindLanguageCardPresenter(LanguageCardView view) {
        return new LanguageCardPresenterImpl(view);
    }

    @Binds
    abstract LanguageCardView bindLanguageCardView(LanguageCard languageCard);
}
