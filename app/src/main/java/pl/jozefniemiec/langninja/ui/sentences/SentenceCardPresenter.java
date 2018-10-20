package pl.jozefniemiec.langninja.ui.sentences;

import java.util.ArrayList;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void pageChanged(int newPosition);

    void playButtonClicked();

    void deactivatedPlayButtonClicked();

    void onReaderInit(boolean isWorking);

    void onStartOfRead();

    void onEndOfRead();

    void onReadError();

    void speechRecognizerButtonClicked();

    void highlightedSpeechButtonClicked();

    void deactivatedSpeechButtonClicked();

    void onSpeechRecognizerInit(boolean recognitionAvailable);

    void onReadyForSpeech();

    void onSpeechEnded();

    void onSpeechError(int errorCode);

    void onSpeechResults(ArrayList<String> spokenTextsList);

    void onViewPause();

    void onViewDestroy();
}
