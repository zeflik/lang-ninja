package pl.jozefniemiec.langninja.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {

    private static final String PUNCTUATION_MARKS_REGEX = "[-+\",.!¡?¿:;]";

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
}
