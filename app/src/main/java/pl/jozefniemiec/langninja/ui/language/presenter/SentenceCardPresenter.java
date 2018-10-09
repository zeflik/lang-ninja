package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

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

    void unHighlightedMicrophoneButtonClicked();

    void highlightedMicrophoneButtonClicked();

    void deactivatedMicrophoneButtonClicked();

    void onSpeechRecognizerInit(boolean recognitionAvailable);

    void onReadyForSpeech();

    void onSpeechEnded();

    void onSpeechError(int errorCode);

    void onSpeechResults(ArrayList<String> spokenTextsList);

    void onViewPause();

    void onViewDestroy();
}
