package pl.jozefniemiec.langninja.ui.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class LoginPresenter implements LoginActivityContract.Presenter {

    private final LoginActivityContract.View view;
    private final FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Inject
    public LoginPresenter(LoginActivityContract.View view, FirebaseAuth auth) {
        this.view = view;
        this.auth = auth;
    }

    @Override
    public void onCreate() {
        //TODO add internet connection check
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                view.close();
            }
        };
    }

    @Override
    public void onResume() {
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onPause() {
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onEmailSignInClicked() {
        //TODO
        view.close();
    }

    @Override
    public void onAutoSignInClicked() {
        view.showAutoSignInPage();
    }

    @Override
    public void onLoginSucceed() {
        view.close();
    }

    @Override
    public void onLoginFailed() {
        //TODO
    }
}
