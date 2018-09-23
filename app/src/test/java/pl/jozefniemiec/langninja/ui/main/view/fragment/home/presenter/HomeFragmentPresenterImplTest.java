package pl.jozefniemiec.langninja.ui.main.view.fragment.home.presenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragmentView;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeFragmentPresenterImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    List<Language> languages;
    @Mock
    private HomeFragmentView view;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private ResourcesManager resourcesManager;
    @InjectMocks
    private HomeFragmentPresenterImpl presenter;

    @Before
    public void setUp() {
        languages = new ArrayList<>();
        languages.add(new Language("pl_PL", "Polski"));
        languages.add(new Language("en_EN", "English"));
        languages.add(new Language("de_DE", "Deutsch"));
        when(languageRepository.getAll()).thenReturn(languages);
    }

    @Test
    public void loadLanguagesAndPassToView() {
        presenter.loadLanguages();
        verify(view).showLanguages(languages);
    }

    @Test
    public void findLanguageCodeOnItemClickAndPassToViewToShowDetails() {
        presenter.loadLanguages();
        int position = 1;
        presenter.onLanguageItemClicked(position);
        verify(view).showLanguageDetails(languages.get(position).getCode());
    }

    @Test
    public void onBindLanguageItemViewAtPosition() {
    }

    @Test
    public void getLanguageItemsCount() {
    }
}