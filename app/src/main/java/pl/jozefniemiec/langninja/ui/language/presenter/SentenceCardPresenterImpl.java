package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.service.ApplicationsManager;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

public class SentenceCardPresenterImpl implements SentenceCardPresenter {

    private final SentenceCardView view;
    private final ResourcesManager resourcesManager;
    private final SentenceRepository sentenceRepository;
    private final ApplicationsManager applicationsManager;
    private String languageCode;
    private List<Sentence> sentences;
    private int currentPosition;
    private String speechRecognizerErrorMessage;

    public SentenceCardPresenterImpl(SentenceCardView view,
                                     ResourcesManager resourcesManager,
                                     SentenceRepository sentenceRepository,
                                     ApplicationsManager applicationsManager) {
        this.view = view;
        this.resourcesManager = resourcesManager;
        this.sentenceRepository = sentenceRepository;
        this.applicationsManager = applicationsManager;
    }

    @Override
    public void loadData(String languageCode) {
        this.languageCode = languageCode;
        sentences = sentenceRepository.getLanguageSentences(languageCode);
        view.showData();
        view.showNumbering(1 + " / " + getPageCount());
        int speechRecognizerAvailable =
                applicationsManager.checkForApplication("com.google.android.googlequicksearchbox");
        switch (speechRecognizerAvailable) {
            case ApplicationsManager.INSTALLED_ENABLED:
                view.activateSpeechRecognizer();
                break;
            case ApplicationsManager.INSTALLED_DISABLED:
                speechRecognizerErrorMessage = "Google search zablokowane";
                view.deactivateSpeechButton();
                break;
            case ApplicationsManager.NOT_INSTALLED:
                speechRecognizerErrorMessage = "Google search niedostępne";
                view.deactivateSpeechButton();
                break;
        }
    }

    @Override
    public void loadPageDataAtPosition(int position, SentencesItemView itemView) {
        itemView.setFlagId(resourcesManager.getFlagId(languageCode));
        itemView.setSentence(sentences.get(position).getSentence());
    }

    @Override
    public int getPageCount() {
        return sentences.size();
    }

    @Override
    public void pageChanged(int newPosition) {
        currentPosition = newPosition;
        cancelSpeechListening();
        stopReading();
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
    }

    @Override
    public void playButtonClicked() {
        cancelSpeechListening();
        view.read(sentences.get(currentPosition).getSentence());
    }

    @Override
    public void deactivatedPlayButtonClicked() {
        view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
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
        view.showErrorMessage(resourcesManager.getNeedInternetConnectionMessage());
    }

    @Override
    public void onReadyForSpeech() {
        view.highlightSpeechButton();
    }

    @Override
    public void onSpeechEnded() {
        view.unHighlightSpeechButton();
    }

    @Override
    public void onSpeechResults(ArrayList<String> spokenTextsList) {
        view.showSpokenText(spokenTextsList.get(0));
    }

    @Override
    public void onSpeechError(int errorCode) {
        view.unHighlightSpeechButton();
        String message = resourcesManager.findOnSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
    }

    @Override
    public void deactivatedMicrophoneButtonClicked() {
        view.showErrorMessage(speechRecognizerErrorMessage);
    }

    @Override
    public void highlightedMicrophoneButtonClicked() {
        view.stopSpeechListening();
    }

    @Override
    public void speechRecognizerButtonClicked() {
        stopReading();
        view.startListening(languageCode);
    }

    @Override
    public void onSpeechRecognizerInit(boolean recognitionAvailable) {
        if (recognitionAvailable) {
            view.activateSpeechButton();
        } else {
            speechRecognizerErrorMessage = "Google speech not working";
            view.deactivateSpeechButton();
        }
    }

    @Override
    public void onViewPause() {
        stopReading();
        cancelSpeechListening();
    }

    @Override
    public void onViewDestroy() {
        sentenceRepository.close();
    }

    private void stopReading() {
        if (view.isReading()) {
            view.stopReading();
            view.unHighlightReadButton();
        }
    }

    private void cancelSpeechListening() {
        if (view.isListeningSpeech()) {
            view.cancelSpeechListening();
            view.unHighlightSpeechButton();
        }
    }
}
