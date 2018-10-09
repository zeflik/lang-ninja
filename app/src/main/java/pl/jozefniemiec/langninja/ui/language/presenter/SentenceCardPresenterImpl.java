package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.jozefniemiec.langninja.model.Sentence;
import pl.jozefniemiec.langninja.repository.SentenceRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;

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
        initializeDatabase();
        sentences = sentenceRepository.getLanguageSentences(languageCode);
        view.showData();
        view.showNumbering(1 + " / " + getPageCount());
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
        view.cancelSpeechListening();
        view.unHighlightSpeechButton();
        view.stopReading();
        view.unHighlightReadButton();
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
    }

    @Override
    public void playButtonClicked() {
        view.cancelSpeechListening();
        view.unHighlightSpeechButton();
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
        String message = resourcesManager.findSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
    }

    @Override
    public void deactivatedMicrophoneButtonClicked() {
        view.showErrorMessage("Google speech not instaled");
    }

    @Override
    public void highlightedMicrophoneButtonClicked() {
        view.stopSpeechListening();
    }

    @Override
    public void unHighlightedMicrophoneButtonClicked() {
        view.stopReading();
        view.unHighlightReadButton();
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
        view.stopReading();
        view.cancelSpeechListening();
    }

    @Override
    public void onViewDestroy() {
        sentenceRepository.close();
    }

    private void initializeDatabase() {
        sentenceRepository.insertAll(
                new Sentence("Ala ma kota", "pl_PL", 0),
                new Sentence("Ala ma psa", "pl_PL", 0),
                new Sentence("Danke", "de", 0),
                new Sentence("Thanx", "en_GB", 0),
                new Sentence("Ala ma mrówkę", "pl_PL", 0)
        );
    }
}
