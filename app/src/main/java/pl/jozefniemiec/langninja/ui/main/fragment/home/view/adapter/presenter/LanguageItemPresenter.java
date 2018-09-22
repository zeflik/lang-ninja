package pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.presenter;

import pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.view.LanguageItemView;

public interface LanguageItemPresenter {

    void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

    int getLanguageItemsCount();
}
