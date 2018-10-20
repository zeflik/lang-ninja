package pl.jozefniemiec.langninja.ui.main.languages.di;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentPresenter;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentView;
import pl.jozefniemiec.langninja.utils.Utility;

@Module
public abstract class LanguagesFragmentModule {

    @Provides
    @LanguagesFragmentScope
    static GridLayoutManager provideGridLayoutManager(Context context) {
        int numberOfColumns = Utility.calculateNoOfColumns(context);
        return new GridLayoutManager(context, numberOfColumns);
    }

    @Binds
    @LanguagesFragmentScope
    abstract View.OnClickListener provideOnClickListener(LanguagesFragment homeFragment);

    @Binds
    @LanguagesFragmentScope
    abstract LanguagesFragmentPresenter
    provideHomeFragmentPresenter(LanguagesFragmentPresenterImpl presenter);

    @Binds
    @LanguagesFragmentScope
    abstract LanguagesFragmentView provideHomeFragmentView(LanguagesFragment homeFragment);
}
