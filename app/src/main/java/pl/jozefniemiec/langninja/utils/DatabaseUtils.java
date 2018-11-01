package pl.jozefniemiec.langninja.utils;

import android.content.Context;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pl.jozefniemiec.langninja.R;

public class DatabaseUtils {

    public static void copyDatabaseToDevice(Context context, String databaseName) {
        try (InputStream inputStream = context.getResources().openRawResource(R.raw.langninja_db_1)) {
            File outputFile = new File(context.getDatabasePath(databaseName).getPath());
            FileUtils.copyInputStreamToFile(inputStream, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfDatabaseExistOnDevice(Context context, String databaseName) {
        return context.getDatabasePath(databaseName).exists();
    }
}
