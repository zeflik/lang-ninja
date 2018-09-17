package pl.jozefniemiec.langninja.activities.main.fragments.home.view.adapter.presenter;

import pl.jozefniemiec.langninja.activities.main.fragments.home.view.adapter.view.LanguageItemView;

public interface LanguageItemPresenter {

    void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

    int getLanguageItemsCount();
}
