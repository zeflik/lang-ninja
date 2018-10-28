package pl.jozefniemiec.langninja.ui.sentences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.utils.Utility;

public class SentenceCardPresenterImpl implements SentenceCardPresenter {

    private final SentenceCardView view;
    private final ResourcesManager resourcesManager;
    private final SentenceRepository sentenceRepository;
    private String languageCode;
    private List<Sentence> sentences;
    private int currentPosition;

    public SentenceCardPresenterImpl(SentenceCardView view,
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
        view.showNumbering(1 + " / " + getPageCount());
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
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
    }

    @Override
    public void playButtonClicked() {
        cancelSpeechListening();
        view.read(sentences.get(currentPosition).getSentence());
    }

    @Override
    public void deactivatedPlayButtonClicked() {
        view.stopSpeechListening();
        view.showTTSInstallDialog();
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
        view.stopReading();
        if (view.isSpeechRecognizerAvailable()) {
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
            view.activateSpeechButton();
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
