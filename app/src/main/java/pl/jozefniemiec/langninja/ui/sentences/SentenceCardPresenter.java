package pl.jozefniemiec.langninja.ui.sentences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.utils.Utility;

public class SentenceCardPresenter implements SentenceCardContract.Presenter {

    private final int NUMBERING_SHIFT = 1;

    private final SentenceCardContract.View view;
    private final ResourcesManager resourcesManager;
    private final SentenceRepository sentenceRepository;
    private String languageCode;
    private List<Sentence> sentences;
    private int currentPosition;

    public SentenceCardPresenter(SentenceCardContract.View view,
                                 ResourcesManager resourcesManager,
                                 SentenceRepository sentenceRepository) {
        this.view = view;
        this.resourcesManager = resourcesManager;
        this.sentenceRepository = sentenceRepository;
    }

    @Override
    public void loadData(String languageCode) {
        this.languageCode = languageCode;
        sentences = sentenceRepository.getLanguageSentences(languageCode);
        view.showData();
        view.showNumbering(NUMBERING_SHIFT, getPageCount());
        view.setTitle(resourcesManager.getLanguageName(languageCode));
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
        view.hideSpokenText();
        currentPosition = newPosition;
        cancelSpeechListening();
        stopReading();
        view.showNumbering(newPosition + NUMBERING_SHIFT, getPageCount());
    }

    @Override
    public void readButtonClicked() {
        cancelSpeechListening();
        view.read(sentences.get(currentPosition).getSentence());
    }

    @Override
    public void deactivatedReadButtonClicked() {
        cancelSpeechListening();
        if (!view.isReaderAvailable()) {
            view.showTTSInstallDialog();
        } else {
            view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
        }
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
        String firstResult = spokenTextsList.get(0);
        String currentSentence = sentences.get(currentPosition).getSentence();
        if (firstResult.equalsIgnoreCase(Utility.removePunctationMarks(currentSentence))) {
            view.showCorrectSpokenText(firstResult);
        } else {
            view.showWrongSpokenText(firstResult);
        }
    }

    @Override
    public void onSpeechError(int errorCode) {
        view.unHighlightSpeechButton();
        String message = resourcesManager.findOnSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
    }

    @Override
    public void deactivatedSpeechButtonClicked() {
        stopReading();
        if (view.isSpeechRecognizerAvailable()) {
            view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
            view.activateSpeechRecognizer();
        } else {
            view.showSpeechRecognizerInstallDialog();
        }
    }

    @Override
    public void highlightedSpeechButtonClicked() {
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
            view.findSpeechSupportedLanguages();
        } else {
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

    @Override
    public void onSpeechSupportedLanguages(List<String> supportedLanguageCodes) {
        if (supportedLanguageCodes.contains(languageCode.replace("_", "-"))) {
            view.activateSpeechButton();
        } else {
            view.deactivateSpeechButton();
        }
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
