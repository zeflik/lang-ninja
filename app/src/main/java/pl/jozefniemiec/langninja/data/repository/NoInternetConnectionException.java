package pl.jozefniemiec.langninja.data.repository;

public class NoInternetConnectionException extends RuntimeException {

    public NoInternetConnectionException() {
        super("Brak połączenia.");
    }
}
