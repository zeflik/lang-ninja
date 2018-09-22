package pl.jozefniemiec.langninja.utils;

import android.util.DisplayMetrics;

public class Utility {
    public static int calculateNoOfColumns(DisplayMetrics displayMetrics) {
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 140);
    }
}
