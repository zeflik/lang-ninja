package pl.jozefniemiec.langninja.data.repository.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.utils.InitialData;

@Database(
        entities = {
                Language.class,
                Sentence.class
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
        AppDatabase appDatabase = Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class)
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            getInstance(context).languageDao().insertAll(InitialData.populateLanguages());
                            getInstance(context).sentenceDao().insertAll(InitialData.populateSentences());
                        });
                    }
                })
                .build();
        appDatabase.beginTransaction();
        appDatabase.endTransaction();
        return appDatabase;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract LanguageDao languageDao();

    public abstract SentenceDao sentenceDao();
}



