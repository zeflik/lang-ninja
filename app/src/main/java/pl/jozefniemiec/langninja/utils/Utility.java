package pl.jozefniemiec.langninja.utils;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

public class Utility {

    private static final String PUNCTUATION_MARKS_REGEX = "[-+\",.!¡?¿:;]";
    private static final String ANDROID_DRAWABLE_URI = "android.resource://%s/drawable/%s";

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 140);
    }

    public static String removePunctationMarks(String text) {
        return text
                .replaceAll(PUNCTUATION_MARKS_REGEX, "")
                .trim()
                .replaceAll("( )+", " ");
    }

    public static Uri getLanguageFlagUri(Context context, String languageCode) {
        return Uri.parse(String.format(ANDROID_DRAWABLE_URI, context.getPackageName(), languageCode.toLowerCase()));
    }
}
