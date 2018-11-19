package pl.jozefniemiec.langninja.ui.sentences.card;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.utils.Utility;

public class SentenceCardPresenter implements SentenceCardContract.Presenter {

    private final int NUMBERING_SHIFT = 1;
    private final String TAG = SentenceCardPresenter.class.getSimpleName();

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
        Log.d(TAG, "loadData: " + languageCode);
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
    public String getCurrentSentence() {
        return sentences.get(currentPosition).getSentence();
    }

    @Override
    public void pageChanged(int newPosition) {
        view.hideSpokenText();
        currentPosition = newPosition;
        cancelSpeechListening();
        view.showNumbering(newPosition + NUMBERING_SHIFT, getPageCount());
    }

    @Override
    public void onReadyForSpeech() {
        view.highlightSpeechButton();
        Log.d(TAG, "onReadyForSpeech: ");
    }

    @Override
    public void onSpeechEnded() {
        view.unHighlightSpeechButton();
        Log.d(TAG, "onSpeechEnded: ");
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
        Log.d(TAG, "onSpeechResults: " + firstResult);
    }

    @Override
    public void onSpeechError(int errorCode) {
        view.unHighlightSpeechButton();
        String message = resourcesManager.findOnSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
        Log.d(TAG, "onSpeechError: " + message);
    }

    @Override
    public void deactivatedSpeechButtonClicked() {
        if (view.isSpeechRecognizerAvailable()) {
            view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
            view.activateSpeechRecognizer();
            Log.d(TAG, "deactivatedSpeechButtonClicked: activating Speech");
        } else {
            view.showSpeechRecognizerInstallDialog();
            Log.d(TAG, "deactivatedSpeechButtonClicked: installing");
        }
    }

    @Override
    public void highlightedSpeechButtonClicked() {
        view.stopSpeechListening();
        Log.d(TAG, "highlightedSpeechButtonClicked: ");
    }

    @Override
    public void speechRecognizerButtonClicked() {
        view.startListening(languageCode);
        Log.d(TAG, "speechRecognizerButtonClicked: start listening " + languageCode);
    }

    @Override
    public void onSpeechRecognizerInit(boolean recognitionAvailable) {
        if (recognitionAvailable) {
            view.findSpeechSupportedLanguages();
        } else {
            view.deactivateSpeechButton();
        }
        Log.d(TAG, "onSpeechRecognizerInit: recognition available: " + recognitionAvailable);
    }

    @Override
    public void onViewPause() {
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
        Log.d(TAG, "onSpeechSupportedLanguages: ");
    }

    private void cancelSpeechListening() {
        if (view.isListeningSpeech()) {
            view.cancelSpeechListening();
            view.unHighlightSpeechButton();
        }
    }
}
