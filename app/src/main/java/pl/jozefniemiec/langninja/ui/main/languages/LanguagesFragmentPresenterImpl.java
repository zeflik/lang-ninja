package pl.jozefniemiec.langninja.ui.main.languages;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;

public class LanguagesFragmentPresenterImpl implements LanguagesFragmentContract.Presenter {

    private final LanguagesFragmentContract.View view;
    private final LanguageRepository languageRepository;
    private List<Language> languages = new ArrayList<>();
    private final ResourcesManager resourcesManager;

    @Inject
    LanguagesFragmentPresenterImpl(LanguagesFragmentContract.View view,
                                   LanguageRepository languageRepository,
                                   ResourcesManager resourcesManager) {
        this.view = view;
        this.languageRepository = languageRepository;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void loadLanguages() {
        try {
            languages.addAll(languageRepository.getAll());
            view.showLanguages(languages);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onLanguageItemClicked(int position) {
        view.showLanguageDetails(languages.get(position).getCode());
    }

    @Override
    public void onBindLanguageItemViewAtPosition(int position, LanguageItemView itemView) {
        itemView.setLanguageNativeName(languages.get(position).getNativeName());
        itemView.setLanguageFlag(resourcesManager.getFlagId(languages.get(position).getCode().toLowerCase()));
        itemView.setLanguageName(resourcesManager.getLanguageName(languages.get(position).getCode()));
    }

    @Override
    public int getLanguageItemsCount() {
        return languages.size();
    }
}






