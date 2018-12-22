package pl.jozefniemiec.langninja.service;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class FirebaseAuthService implements AuthService {

    private FirebaseAuth instance;

    @Inject
    FirebaseAuthService() {
        instance = FirebaseAuth.getInstance();
    }

    @Override
    public boolean checkSignInStatus() {
        return instance.getCurrentUser() != null;
    }

    @Override
    public String getCurrentUserUid() {
        return instance.getCurrentUser().getUid();
    }

    @Override
    public String getCurrentUserName() {
        return instance.getCurrentUser().getDisplayName();
    }

    @Override
    public String getCurrentUserEmail() {
        return instance.getCurrentUser().getEmail();
    }
}
