package pl.jozefniemiec.langninja.voice;

public interface ReaderListener {

    void onReaderInit(boolean status);

    void onEndOfRead();

    void onReadError();

    void onStartOfRead();
}