package pl.jozefniemiec.langninja.repository;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.model.Sentence;
import pl.jozefniemiec.langninja.repository.room.AppDatabase;
import pl.jozefniemiec.langninja.repository.room.SentenceDao;

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
}
