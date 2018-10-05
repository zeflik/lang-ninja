package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void pageChanged(int newPosition);

    void playButtonClicked();

    void hiddenPlayButtonClicked();

    void readerInitialized();

    void readerReady();

    void readerLanguageNotSupported();

    void unHighlightedMicrophoneButtonClicked();

    void speechListening();

    void spokenText(ArrayList<String> spokenTextsList);

    void speechEnded();

    void speechError(int string);

    void highlightedMicrophoneButtonClicked();

    void speechAvailable(boolean isAvailable);

    void deactivatedMicrophoneButtonClicked();

    void readerNotInitialized();
}
