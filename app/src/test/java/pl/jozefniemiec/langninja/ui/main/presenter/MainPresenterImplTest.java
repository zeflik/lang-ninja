package pl.jozefniemiec.langninja.ui.main.presenter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.main.MainPresenter;
import pl.jozefniemiec.langninja.ui.main.MainView;

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
    private MainPresenter presenter;

    @Test
    public void showFragmentsInView() {
        presenter.loadMain();
        verify(view).showFragments();
    }

    @Test
    public void closeRepositoryOnDestroy() {
        verify(languageRepository).close();
    }

    @Test
    public void createDBInitialData() {
        presenter.loadMain();
        verify(languageRepository).insertAll(any(Language.class));
    }
}