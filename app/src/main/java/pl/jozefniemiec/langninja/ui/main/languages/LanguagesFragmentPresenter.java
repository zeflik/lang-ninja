package pl.jozefniemiec.langninja.ui.main.languages;

public interface LanguagesFragmentPresenter {

    void loadLanguages();

    void onLanguageItemClicked(int position);

    void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView);

    int getLanguageItemsCount();
}
