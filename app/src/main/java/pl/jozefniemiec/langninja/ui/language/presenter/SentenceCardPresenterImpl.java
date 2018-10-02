package pl.jozefniemiec.langninja.ui.language.presenter;

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
        view.stopSpeaking();
        view.showNumbering(newPosition + 1 + " / " + getPageCount());
    }

    @Override
    public void playButtonClicked() {
        view.speak(sentences.get(currentPosition).getSentence());
    }

    @Override
    public void readerInitialized(boolean isWorking) {
        if (isWorking) {
            view.setReaderLanguage(sentences.get(currentPosition).getLanguageCode());
            view.showPlayButton();
        } else {
            view.showErrorMessage("Reader not initialized");
            view.hidePlayButton();
        }
    }

    @Override
    public void readerLanguageNotSupported(String languageCode) {
        view.showErrorMessage("Language " + languageCode + "not supported");
    }

    @Override
    public void microphoneButtonClicked() {
        view.stopSpeaking();
        view.speechListen(languageCode);
    }

    @Override
    public void spokenText(ArrayList<String> spokenTextsList) {
        view.showSpokenText(spokenTextsList.get(0));
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
