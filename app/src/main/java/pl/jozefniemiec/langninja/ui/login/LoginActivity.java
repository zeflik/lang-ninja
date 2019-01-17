package pl.jozefniemiec.langninja.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.base.BaseActivity;
import pl.jozefniemiec.langninja.utils.Utility;

public class LoginActivity extends BaseActivity implements LoginActivityContract.View {

    private static final int RC_SIGN_IN = 1;

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
    public void login() {
        if (Utility.isNetworkAvailable(this)) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()
                            ))
                            .build(),
                    RC_SIGN_IN);
        } else {
            presenter.onMissingInternetConnection();
        }
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
    public void showNeedInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.missing_internet_connection)
                .setMessage(R.string.message_connect_to_internet_and_refresh)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.button_refresh, (dialog, whichButton) -> {
                    presenter.refreshInternetButtonClicked();
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> this.finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void notifyDataChanged() {
        Utility.sendBroadcastUserSentencesChanged(this);
    }

    @Override
    public void showLoginErrorMessage() {
        Toast.makeText(this, R.string.message_login_error, Toast.LENGTH_SHORT).show();
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

