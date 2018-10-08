package pl.jozefniemiec.langninja.ui.language.presenter;

import java.util.ArrayList;
import java.util.List;

import pl.jozefniemiec.langninja.model.Sentence;
import pl.jozefniemiec.langninja.repository.SentenceRepository;
import pl.jozefniemiec.langninja.resources.ResourcesManager;
import pl.jozefniemiec.langninja.ui.language.view.SentenceCardView;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesItemView;
import pl.jozefniemiec.langninja.voice.Reader;
import pl.jozefniemiec.langninja.voice.ReaderListener;

public class SentenceCardPresenterImpl implements SentenceCardPresenter, ReaderListener {

    private static final String TAG = "SentenceCardPresenter";
    private final SentenceCardView view;
    private final ResourcesManager resourcesManager;
    private final SentenceRepository sentenceRepository;
    private final Reader reader;
    private String languageCode;
    private List<Sentence> sentences;
    private int currentPosition;

    public SentenceCardPresenterImpl(SentenceCardView view,
                                     ResourcesManager resourcesManager,
                                     SentenceRepository sentenceRepository,
                                     Reader reader) {
        this.view = view;
        this.resourcesManager = resourcesManager;
        this.sentenceRepository = sentenceRepository;
        this.reader = reader;
        reader.setOnReadListener(this);
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
        view.cancelSpeechListening();
        view.unHighlightSpeakButton();
        reader.stop();
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
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
    public void unHighlightedMicrophoneButtonClicked() {
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

    @Override
    public void onReaderInit(boolean isWorking) {
        if (isWorking && reader.setLanguage(languageCode)) {
            view.showReadButton();
        } else {
            view.hideReadButton();
        }
    }

    @Override
    public void playButtonClicked() {
        reader.read(sentences.get(currentPosition).getSentence());
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
    public void hiddenPlayButtonClicked() {
        view.showErrorMessage(resourcesManager.getLanguageNotSupportedMessage());
    }

    @Override
    public void onViewPause() {
        reader.stop();
    }

    @Override
    public void onViewDestroy() {
        reader.shutdown();
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
