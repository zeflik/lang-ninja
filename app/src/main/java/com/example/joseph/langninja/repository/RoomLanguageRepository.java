package com.example.joseph.langninja.repository;

import android.content.Context;

import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.repository.room.AppDatabase;
import com.example.joseph.langninja.repository.room.LanguageDao;

import java.util.List;

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
