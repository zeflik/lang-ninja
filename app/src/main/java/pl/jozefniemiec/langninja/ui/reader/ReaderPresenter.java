package pl.jozefniemiec.langninja.ui.reader;

import java.util.Locale;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.reader.ReaderFragmentScope;

@ReaderFragmentScope
public class ReaderPresenter implements ReaderContract.Presenter {

    private static final String TAG = ReaderPresenter.class.getSimpleName();
    private final ReaderContract.View view;
    private final ResourcesManager resourcesManager;
    private String languageCode;

    @Inject
    ReaderPresenter(ReaderContract.View view, ResourcesManager resourcesManager) {
        this.view = view;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void onViewCreated(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public void onReaderInit(boolean isWorking) {
        if (isWorking && view.setReaderLanguage(new Locale(languageCode))) {
            view.activateReadButton();
        } else {
            view.deactivateReadButton();
        }
    }

    @Override
    public void readButtonClicked() {
        view.showProgressBar();
        view.read(view.getCurrentSentence());
    }

    @Override
    public void onStartOfRead() {
        view.highlightReadButton();
        view.hideProgressBar();
    }

    @Override
    public void onEndOfRead() {
        view.unHighlightReadButton();
    }

    @Override
    public void onReadError() {
        view.showErrorMessage(resourcesManager.getNeedInternetConnectionMessage());
        view.unHighlightReadButton();
        view.hideProgressBar();
    }

    @Override
    public void deactivatedReadButtonClicked() {
        if (!view.isReaderAvailable()) {
            view.showTTSInstallDialog();
        } else {
            view.showErrorMessage("Language Not supported");//resourcesManager.getLanguageNotSupportedMessage());
        }
    }
}
