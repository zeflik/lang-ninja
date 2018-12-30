package pl.jozefniemiec.langninja.di.sentences.community;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.sentences.community.CommunityCardFragment;

@Module
public abstract class CommunityCardProvider {

    @ContributesAndroidInjector(modules = CommunityCardModule.class)
    @CommunityCardScope
    abstract CommunityCardFragment provideCommunityCardFragment();
}
