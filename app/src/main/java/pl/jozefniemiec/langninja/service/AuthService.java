package pl.jozefniemiec.langninja.service;

public interface AuthService {

    boolean checkSignInStatus();

    String getCurrentUserUid();
}
