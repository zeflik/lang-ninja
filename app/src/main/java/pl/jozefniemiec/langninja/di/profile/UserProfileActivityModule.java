package pl.jozefniemiec.langninja.di.profile;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.profile.UserProfileActivity;
import pl.jozefniemiec.langninja.ui.profile.UserProfileContract;
import pl.jozefniemiec.langninja.ui.profile.UserProfilePresenter;

@Module
public abstract class UserProfileActivityModule {

    @Binds
    @UserProfileActivityScope
    abstract UserProfileContract.Presenter provideUserProfilePresenter(UserProfilePresenter presenter);

    @Binds
    @UserProfileActivityScope
    abstract UserProfileContract.View provideUserProfileActivityyView(UserProfileActivity view);
}
