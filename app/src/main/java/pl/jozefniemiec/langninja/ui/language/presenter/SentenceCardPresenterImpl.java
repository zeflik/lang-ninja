package pl.jozefniemiec.langninja.ui.language.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        itemView.setFlag(resourcesManager.getFlagId(languageCode));
        itemView.setSentence(sentences.get(position).getSentence());
    }

    @Override
    public int getPageCount() {
        return sentences.size();
    }

    @Override
    public void pageChanged(int newPosition) {
        currentPosition = newPosition;
        view.stopReading();
        view.cancelSpeechListening();
        view.unHighlightSpeakButton();
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
    }

    @Override
    public void playButtonClicked() {
        view.cancelSpeechListening();
        view.unHighlightSpeakButton();
        int status = view.read(sentences.get(currentPosition).getSentence());
        if (status != 0) {
            view.showErrorMessage("Reader error");
            view.hideReadButton();
        }
    }

    @Override
    public void hiddenPlayButtonClicked() {
        view.showErrorMessage("Language not supported");
    }

    @Override
    public void readerInitialized() {
        view.setReaderLanguage(sentences.get(currentPosition).getLanguageCode());
    }

    @Override
    public void readerReady() {
        view.showReadButton();
    }

    @Override
    public void readerLanguageNotSupported() {
        view.showErrorMessage("Language " + languageCode + "not supported");
        view.hideReadButton();
        Log.d("reader", "not supported");
    }

    @Override
    public void speechAvailable(boolean isAvailable) {
        if (isAvailable) {
            view.activateSpeechButton();
        } else {
            view.deactivateSpeechButton();
        }
    }

    @Override
    public void deactivatedMicrophoneButtonClicked() {
        view.showErrorMessage("Google speech not instaled");
    }

    @Override
    public void readerNotInitialized() {
        view.showErrorMessage("Reader is not available");
    }

    @Override
    public void unHighlightedMicrophoneButtonClicked() {
        view.stopReading();
        view.listenSpeech(languageCode);
    }

    @Override
    public void highlightedMicrophoneButtonClicked() {
        view.stopSpeechListening();
    }

    @Override
    public void spokenText(ArrayList<String> spokenTextsList) {
        view.showSpokenText(spokenTextsList.get(0));
    }

    @Override
    public void speechListening() {
        view.highlightSpeakButton();
    }

    @Override
    public void speechEnded() {
        view.unHighlightSpeakButton();
    }

    @Override
    public void speechError(int errorCode) {
        view.unHighlightSpeakButton();
        String message = resourcesManager.findSpeechErrorMessage(errorCode);
        view.showErrorMessage(message);
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
