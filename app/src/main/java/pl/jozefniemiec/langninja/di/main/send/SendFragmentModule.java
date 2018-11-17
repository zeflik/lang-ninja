package pl.jozefniemiec.langninja.di.main.send;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.main.send.SendFragment;
import pl.jozefniemiec.langninja.ui.main.send.SendFragmentContract;
import pl.jozefniemiec.langninja.ui.main.send.SendPresenter;

@Module
abstract class SendFragmentModule {

    @Binds
    @SendFragmentScope
    abstract SendFragmentContract.Presenter providePresenter(SendPresenter presenter);

    @Binds
    @SendFragmentScope
    abstract SendFragmentContract.View provideHomeFragmentView(SendFragment fragment);
}
