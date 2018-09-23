package pl.jozefniemiec.langninja.ui.main.presenter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.ui.main.view.MainView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class MainPresenterImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MainView view;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private MainPresenterImpl presenter;

    @Test
    public void showFragmentsInView() {
        presenter.loadMain();
        verify(view).showFragments();
    }

    @Test
    public void closeRepositoryOnDestroy() {
        presenter.onExitCleanup();
        verify(languageRepository).close();
    }

    @Test
    public void createDBInitialData() {
        presenter.loadMain();
        verify(languageRepository).insertAll(any(Language.class));
    }
}