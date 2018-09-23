package pl.jozefniemiec.langninja.repository.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.model.Sentence;

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
        return Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract LanguageDao languageDao();

    public abstract SentenceDao sentenceDao();
}



