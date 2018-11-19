package pl.jozefniemiec.langninja.ui.sentences.reader;

import java.util.Locale;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.sentences.reader.ReaderFragmentScope;

@ReaderFragmentScope
public class ReaderPresenter implements ReaderFragmentContract.Presenter {

    private static final String TAG = ReaderPresenter.class.getSimpleName();
    private final ReaderFragmentContract.View view;
    private String languageCode;

    @Inject
    ReaderPresenter(ReaderFragmentContract.View view) {
        this.view = view;
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
        view.read(view.getCurrentSentence());
    }

    @Override
    public void onStartOfRead() {
        view.highlightReadButton();
    }

    @Override
    public void onEndOfRead() {
        view.unHighlightReadButton();
    }

    @Override
    public void onReadError() {
        view.unHighlightReadButton();
        view.showErrorMessage("Error");//resourcesManager.getNeedInternetConnectionMessage());
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
