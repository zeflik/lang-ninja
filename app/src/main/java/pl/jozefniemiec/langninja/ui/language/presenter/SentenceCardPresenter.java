package pl.jozefniemiec.langninja.ui.language.presenter;

import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public interface SentenceCardPresenter {

    void loadData(String languageCode);

    void loadPageDataAtPosition(int position, SentencesItemView itemView);

    int getPageCount();

    void onPageChange(int position);
}
