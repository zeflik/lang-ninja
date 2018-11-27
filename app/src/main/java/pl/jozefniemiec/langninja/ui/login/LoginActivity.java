package pl.jozefniemiec.langninja.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;

public class LoginActivity extends DaggerAppCompatActivity implements LoginActivityContract.View {

    private static final int RC_SIGN_IN = 1;

    @BindView(R.id.email)
    EditText mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @OnClick(R.id.email_sign_in_button)
    void emailSignIn() {
        presenter.onEmailSignInClicked();
    }

    @OnClick(R.id.auto_sign_in_button)
    void autoSignIn() {
        presenter.onAutoSignInClicked();
    }

    @Inject
    LoginActivityContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter.onCreate();
    }

    @Override
    public void showAutoSignInPage() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                presenter.onLoginSucceed();
            } else if (resultCode == RESULT_CANCELED) {
                presenter.onLoginFailed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void close() {
        finish();
    }
}

