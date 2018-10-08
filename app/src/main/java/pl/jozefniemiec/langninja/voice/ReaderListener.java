package pl.jozefniemiec.langninja.voice;

public interface ReaderListener {

    void onReaderInit(boolean status);

    void onStartOfRead();

    void onEndOfRead();

    void onReadError();
}