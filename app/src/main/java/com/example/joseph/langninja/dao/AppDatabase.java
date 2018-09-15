package com.example.joseph.langninja.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.joseph.langninja.model.Language;

@Database(
        entities = {
                Language.class
        },
        version = 1)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class)
//                .addCallback(new Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        Executors.newSingleThreadScheduledExecutor()
//                                .execute(() -> getInstance(context).basicTaskDao()
//                                        .insertAll(InitialData.populateData()));
//                    }
//                })
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract LanguageDao languageDao();

}


