package pl.jozefniemiec.langninja.ui.main.languages.presenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.main.languages.LanguageItemView;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentPresenterImpl;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragmentView;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LanguagesFragmentPresenterImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    List<Language> languages;
    @Mock
    private LanguagesFragmentView view;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private ResourcesManager resourcesManager;
    @InjectMocks
    private LanguagesFragmentPresenterImpl presenter;
    @Mock
    private LanguageItemView itemView;

    @Before
    public void setUp() {
        languages = new ArrayList<>();
        languages.add(new Language("pl_PL", "Polski"));
        languages.add(new Language("en_EN", "English"));
        languages.add(new Language("de_DE", "Deutsch"));
        when(languageRepository.getAll()).thenReturn(languages);
        presenter.loadLanguages();
    }

    @Test
    public void passDataToView() {
        verify(view).showLanguages(languages);
    }

    @Test
    public void findLanguageCodeOnItemClickAndPassToViewToShowDetails() {
        int position = 1;
        presenter.onLanguageItemClicked(position);
        verify(view).showLanguageDetails(languages.get(position).getCode());
    }

    @Test
    public void passResourcesToItemViewAtPosition() {
        int position = 2;
        String langCode = languages.get(position).getCode();
        when(resourcesManager.getLanguageName(langCode)).thenReturn("Lang Name");
        when(resourcesManager.getFlagId(langCode.toLowerCase())).thenReturn(123);
        presenter.onBindLanguageItemViewAtPosition(position, itemView);
        verify(itemView).setLanguageName("Lang Name");
        verify(itemView).setLanguageFlag(123);
    }


    @Test
    public void passDataToItemViewAtPosition() {
        int position = 0;
        presenter.onBindLanguageItemViewAtPosition(position, itemView);
        verify(itemView).setLanguageNativeName(languages.get(position).getNativeName());
    }

    @Test
    public void provideLanguageItemsCount() {
        assertEquals(languages.size(), presenter.getLanguageItemsCount());
    }
}