package pl.jozefniemiec.langninja.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.login.LoginActivity;

public abstract class BaseSecuredActivity extends BaseActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        auth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                showLoginDialog();
            }
        };
    }

    private void showLoginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Wymagana autoryzacja")
                .setMessage("Zawartość tylko dla zarejstrowanych użytkowników. Czy chcesz się zalogować?")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton(R.string.login, (dialog, whichButton) -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> ((Activity) context).finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
