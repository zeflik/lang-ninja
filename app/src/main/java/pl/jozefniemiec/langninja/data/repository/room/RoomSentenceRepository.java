package pl.jozefniemiec.langninja.data.repository.room;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public class RoomSentenceRepository implements SentenceRepository {

    private final Context context;
    private final SentenceDao sentenceDao;

    @Inject
    public RoomSentenceRepository(Context context) {
        this.context = context;
        this.sentenceDao = AppDatabase.getInstance(context).sentenceDao();
    }

    @Override
    public List<Sentence> getLanguageSentences(String languageCode) {
        return sentenceDao.getSentences(languageCode);
    }

    @Override
    public void insertAll(Sentence... sentences) {
        sentenceDao.insertAll(sentences);
    }

    @Override
    public void close() {
        AppDatabase.destroyInstance();
    }

    @Override
    public void update(Sentence sentence) {
        sentenceDao.update(sentence);
    }

    @Override
    public void delete(Sentence sentence) {
        sentenceDao.delete(sentence);
    }
}
