package pl.jozefniemiec.langninja.storage;

public class NoInternetException extends RuntimeException {

    NoInternetException(String message) {
        super(message);
    }
}
