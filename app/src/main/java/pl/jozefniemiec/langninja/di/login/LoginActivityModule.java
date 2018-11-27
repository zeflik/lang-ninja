package pl.jozefniemiec.langninja.di.login;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.login.LoginActivity;
import pl.jozefniemiec.langninja.ui.login.LoginActivityContract;
import pl.jozefniemiec.langninja.ui.login.LoginPresenter;

@Module
public abstract class LoginActivityModule {

    @Binds
    @LoginActivityScope
    abstract LoginActivityContract.Presenter provideLoginPresenter(LoginPresenter presenter);

    @Binds
    @LoginActivityScope
    abstract LoginActivityContract.View provideLoginActivityView(LoginActivity view);
}
