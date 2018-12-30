package pl.jozefniemiec.langninja.di.sentences.community;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.sentences.community.CommunityCardContract;
import pl.jozefniemiec.langninja.ui.sentences.community.CommunityCardFragment;
import pl.jozefniemiec.langninja.ui.sentences.community.CommunityCardPresenter;

@Module
abstract class CommunityCardModule {

    @Binds
    @CommunityCardScope
    abstract CommunityCardContract.Presenter providePresenter(CommunityCardPresenter presenter);

    @Binds
    @CommunityCardScope
    abstract CommunityCardContract.View provideView(CommunityCardFragment fragment);
}
