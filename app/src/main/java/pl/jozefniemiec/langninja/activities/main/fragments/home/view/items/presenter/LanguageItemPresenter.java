package pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.presenter;

import pl.jozefniemiec.langninja.activities.main.fragments.home.view.items.view.LanguageItemView;

public interface LanguageItemPresenter {

    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

    public int getLanguageItemsCount();
}
