package pl.jozefniemiec.langninja.di.main.community;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.community.CommunityFragment;
import pl.jozefniemiec.langninja.ui.main.community.CommunityFragmentContract;
import pl.jozefniemiec.langninja.ui.main.community.CommunityPresenter;

@Module
abstract class CommunityFragmentModule {

    @Binds
    @CommunityFragmentScope
    abstract CommunityFragmentContract.Presenter providePresenter(CommunityPresenter presenter);

    @Binds
    @CommunityFragmentScope
    abstract CommunityFragmentContract.View provideView(CommunityFragment fragment);
}
