package pl.jozefniemiec.langninja.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageButton;

public class Utility {

    private static final String PUNCTUATION_MARKS_REGEX = "[-+\",.!¡?¿:;]";
    private static final String ANDROID_DRAWABLE_URI = "android.resource://%s/drawable/%s";
    private static final int HIGHLIGHT_BUTTON_COLOR = Color.GREEN;
    private static final int INACTIVE_BUTTON_COLOR = Color.LTGRAY;

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

    public static void grayOutButton(Activity activity, ImageButton imageButton) {
        activity.runOnUiThread(() -> imageButton.getBackground().setColorFilter(INACTIVE_BUTTON_COLOR, PorterDuff.Mode.SRC_ATOP));
    }

    public static void showButton(Activity activity, ImageButton imageButton) {
        activity.runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
    }

    public static void highlightButton(Activity activity, ImageButton imageButton) {
        activity.runOnUiThread(() -> imageButton.getBackground().setColorFilter(HIGHLIGHT_BUTTON_COLOR, PorterDuff.Mode.SRC_ATOP));
    }

    public static void unHighlightButton(Activity activity, ImageButton imageButton) {
        activity.runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
    }
}
