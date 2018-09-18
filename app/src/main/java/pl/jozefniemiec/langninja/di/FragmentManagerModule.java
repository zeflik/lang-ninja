package pl.jozefniemiec.langninja.di;


import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentManagerModule {

    private final FragmentManager fragmentManager;

    public FragmentManagerModule(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Provides
    public FragmentManager fragmentManager() {
        return fragmentManager;
    }
}
