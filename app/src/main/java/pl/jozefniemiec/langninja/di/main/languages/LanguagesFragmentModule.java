package pl.jozefniemiec.langninja.di.main.languages;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentContract;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentPresenter;
import pl.jozefniemiec.langninja.utils.Utility;

@Module
abstract class LanguagesFragmentModule {

    @Provides
    @LanguagesFragmentScope
    static GridLayoutManager provideGridLayoutManager(Context context) {
        int numberOfColumns = Utility.calculateNoOfColumns(context);
        return new GridLayoutManager(context, numberOfColumns);
    }

    @Binds
    @LanguagesFragmentScope
    abstract LanguagesFragmentContract.Presenter
    provideHomeFragmentPresenter(LanguagesFragmentPresenter presenter);

    @Binds
    @LanguagesFragmentScope
    abstract LanguagesFragmentContract.View provideHomeFragmentView(LanguagesFragment homeFragment);
}
