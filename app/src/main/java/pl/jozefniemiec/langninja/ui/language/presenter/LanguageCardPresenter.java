package pl.jozefniemiec.langninja.ui.language.presenter;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public interface LanguageCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();
}
