package pl.jozefniemiec.langninja.data.repository.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.Executors;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;
import pl.jozefniemiec.langninja.data.utils.InitialDataDebug;

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
        AppDatabase appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, "langninja-db-1")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            AppDatabase appDb = getInstance(context);
                            appDb.languageDao().insertAll(InitialDataDebug.populateLanguages());
                            appDb.sentenceDao().insertAll(InitialDataDebug.populateSentences());
                            try (InputStream inputStream = context.getResources().openRawResource(R.raw.langs_data)) {
                                POIFSFileSystem fs = new POIFSFileSystem(inputStream);
                                HSSFWorkbook workbook = new HSSFWorkbook(fs);
                                Iterator<Sheet> sheetIterator = workbook.sheetIterator();
                                while (sheetIterator.hasNext()) {
                                    Sheet sheet = sheetIterator.next();
                                    String langCode = sheet.getSheetName();
                                    String langNativeName = sheet.getRow(0).getCell(0).getStringCellValue();
                                    appDb.languageDao().insertAll(new Language(langCode, langNativeName));
                                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                                        String sentence = sheet.getRow(i).getCell(0).getStringCellValue();
                                        int difficulty = (int) sheet.getRow(i).getCell(1).getNumericCellValue();
                                        appDb.sentenceDao().insertAll(new Sentence(sentence, langCode, difficulty));
                                    }
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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



