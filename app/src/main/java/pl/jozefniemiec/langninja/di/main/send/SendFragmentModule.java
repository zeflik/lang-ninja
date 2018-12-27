package pl.jozefniemiec.langninja.di.main.send;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.community.SendFragment;
import pl.jozefniemiec.langninja.ui.main.community.SendFragmentContract;
import pl.jozefniemiec.langninja.ui.main.community.SendPresenter;

@Module
abstract class SendFragmentModule {

    @Binds
    @SendFragmentScope
    abstract SendFragmentContract.Presenter providePresenter(SendPresenter presenter);

    @Binds
    @SendFragmentScope
    abstract SendFragmentContract.View provideHomeFragmentView(SendFragment fragment);
}
