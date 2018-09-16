package pl.jozefniemiec.langninja.repository;

import android.content.Context;

import java.util.List;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.room.AppDatabase;
import pl.jozefniemiec.langninja.repository.room.LanguageDao;

public class RoomLanguageRepository implements LanguageRepository {

    public static final String ROOM_DATABASE_ERROR_MESSAGE = "Room Database Error";

    private final Context context;
    private final LanguageDao languageDao;

    public RoomLanguageRepository(Context context) {
        this.context = context;
        this.languageDao = AppDatabase.getInstance(context).languageDao();
    }

    @Override
    public List<Language> getAll() throws RuntimeException {
        try {
            return languageDao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(ROOM_DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public void insertAll(Language... languages) {
        try {
            languageDao.insertAll(languages);
        } catch (Exception e) {
            throw new RuntimeException(ROOM_DATABASE_ERROR_MESSAGE);
        }
    }
}