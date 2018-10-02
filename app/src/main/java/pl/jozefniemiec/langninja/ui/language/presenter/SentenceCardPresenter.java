package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void pageChanged(int newPosition);

    void playButtonClicked();

    void readerInitialized(boolean isWorking);

    void readerLanguageNotSupported(String languageCode);

    void microphoneButtonClicked();

    void spokenText(ArrayList<String> spokenTextsList);
}
