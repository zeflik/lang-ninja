package pl.jozefniemiec.langninja.data.repository.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.utils.DatabaseUtils;

@Database(
        entities = {
                Language.class,
                Sentence.class
        },
        version = 2)

public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "langninja_db";
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final int INITIAL_VERSION = 1;
    private static final int PRODUCTION_VERSION = 2;
    private static AppDatabase INSTANCE;
    private static Migration migration = new Migration(INITIAL_VERSION, PRODUCTION_VERSION) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d(TAG, "Migrating " + database.getPath()
                    + " version " + database.getVersion()
                    + " to version " + PRODUCTION_VERSION);
        }
    };

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {

        if (!DatabaseUtils.checkIfDatabaseExistOnDevice(context, DATABASE_NAME)) {
            DatabaseUtils.copyDatabaseToDevice(context, DATABASE_NAME);
        }

        return Room.databaseBuilder(context,
                AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(migration)
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract LanguageDao languageDao();

    public abstract SentenceDao sentenceDao();
}



