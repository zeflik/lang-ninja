package pl.jozefniemiec.langninja.ui.main.fragment.home.presenter;

import pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.LanguageItemView;

public interface HomeFragmentPresenter {

    void loadLanguages();

    void onLanguageItemClicked(int position);

    void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

    int getLanguageItemsCount();
}
