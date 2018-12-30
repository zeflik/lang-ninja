package pl.jozefniemiec.langninja.ui.base;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.jozefniemiec.langninja.utils.Utility;

public abstract class BaseSecuredActivity extends BaseActivity {

    protected FirebaseAuth auth;
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
        Utility.signInRequiredDialog(this);
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
