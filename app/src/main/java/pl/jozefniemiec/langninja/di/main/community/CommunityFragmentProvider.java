package pl.jozefniemiec.langninja.di.main.community;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.community.CommunityFragment;

@Module
public abstract class CommunityFragmentProvider {

    @ContributesAndroidInjector(modules = CommunityFragmentModule.class)
    @CommunityFragmentScope
    abstract CommunityFragment provideCommunityFragment();
}
