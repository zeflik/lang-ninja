package pl.jozefniemiec.langninja.ui.sentences.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;

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
    public void loadData(String languageCode, String sentence) {
        this.languageCode = languageCode;
        if (sentence == null) {
            sentences = sentenceRepository.getLanguageSentences(languageCode);
        } else {
            sentences = new ArrayList<>(Collections.singletonList(new Sentence(sentence, languageCode)));
        }
        view.showData();
        view.showNumbering(NUMBERING_SHIFT, getPageCount());
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
        currentPosition = newPosition;
        view.showNumbering(newPosition + NUMBERING_SHIFT, getPageCount());
    }

    @Override
    public void onViewDestroy() {
        sentenceRepository.close();
    }

    @Override
    public void setCurrentSentence(String quote) {
        Sentence sentence = sentences.get(0);
        sentence.setSentence(quote);
        sentences.set(0, sentence);
        view.notifyDataChanged();
    }
}
