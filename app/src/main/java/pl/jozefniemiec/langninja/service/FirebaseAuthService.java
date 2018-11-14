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
    public String getCurrentUserUid() throws NullPointerException {
        return instance.getCurrentUser().getUid();
    }
}
