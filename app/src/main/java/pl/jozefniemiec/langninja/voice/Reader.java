package pl.jozefniemiec.langninja.voice;

public interface Reader {

    boolean setLanguage(String string);

    void read(String text);

    void stop();

    void shutdown();

    void setOnReadListener(ReaderListener listener);
}
