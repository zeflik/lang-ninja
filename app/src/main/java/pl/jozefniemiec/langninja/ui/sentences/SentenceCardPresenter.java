package pl.jozefniemiec.langninja.ui.sentences;

import java.util.ArrayList;
import java.util.List;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void pageChanged(int newPosition);

    void readButtonClicked();

    void deactivatedReadButtonClicked();

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

    void onSpeechSupportedLanguages(List<String> stringArrayList);
}
