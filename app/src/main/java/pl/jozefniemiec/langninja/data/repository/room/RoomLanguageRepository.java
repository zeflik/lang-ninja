package pl.jozefniemiec.langninja.data.repository.room;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;

public class RoomLanguageRepository implements LanguageRepository {

    private static final String ROOM_DATABASE_ERROR_MESSAGE = "Room Database Error: ";
    private static final String TAG = RoomLanguageRepository.class.getSimpleName();

    private final LanguageDao languageDao;

    @Inject
    RoomLanguageRepository(Context context) {
        this.languageDao = AppDatabase.getInstance(context).languageDao();
    }

    @Override
    public List<Language> getAll() throws RuntimeException {
        try {
            return languageDao.getAll();
        } catch (Exception e) {
            Log.e(TAG, ROOM_DATABASE_ERROR_MESSAGE + e.getMessage());
            throw new RuntimeException(ROOM_DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public void insertAll(Language... languages) {
        try {
            languageDao.insertAll(languages);
        } catch (Exception e) {
            Log.e(TAG, ROOM_DATABASE_ERROR_MESSAGE + e.getMessage());
            throw new RuntimeException(ROOM_DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public void close() {
        AppDatabase.destroyInstance();
    }
}
