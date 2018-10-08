package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void pageChanged(int newPosition);

    void unHighlightedMicrophoneButtonClicked();

    void speechListening();

    void spokenText(ArrayList<String> spokenTextsList);

    void speechEnded();

    void speechError(int string);

    void highlightedMicrophoneButtonClicked();

    void speechAvailable(boolean isAvailable);

    void deactivatedMicrophoneButtonClicked();

    void playButtonClicked();

    void hiddenPlayButtonClicked();

    void onViewPause();

    void onViewDestroy();
}
