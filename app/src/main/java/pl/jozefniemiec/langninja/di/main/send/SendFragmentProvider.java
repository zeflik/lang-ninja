package pl.jozefniemiec.langninja.di.main.send;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.main.community.SendFragment;

@Module
public abstract class SendFragmentProvider {

    @ContributesAndroidInjector(modules = SendFragmentModule.class)
    @SendFragmentScope
    abstract SendFragment provideSendFragment();
}
