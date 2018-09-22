package pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.presenter;

import java.util.List;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.adapter.view.LanguageItemView;

public class LanguageItemPresenterImpl implements LanguageItemPresenter {

    private final List<Language> languages;
    private final ResourcesManager resourcesManager;

    public LanguageItemPresenterImpl(List<Language> languages, ResourcesManager resourcesManager) {
        this.languages = languages;
        this.resourcesManager = resourcesManager;
    }

    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView) {
        itemView.setLanguageNativeName(languages.get(position).getNativeName());
        itemView.setLanguageFlag(resourcesManager.getFlagId(languages.get(position).getCode().toLowerCase()));
        itemView.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
    }

    public int getLanguageItemsCount() {
        return languages.size();
    }
}
