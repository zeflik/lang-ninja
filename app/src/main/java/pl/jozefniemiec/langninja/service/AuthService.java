package pl.jozefniemiec.langninja.service;

public interface AuthService {

    boolean isSignedIn();

    String getCurrentUserUid();

    String getCurrentUserName();

    String getCurrentUserEmail();
}
