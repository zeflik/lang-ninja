package pl.jozefniemiec.langninja.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.login.LoginActivity;

import static pl.jozefniemiec.langninja.ui.base.Constants.ACTION_USER_SENTENCES_CHANGED;

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

    public static String removePunctuationMarks(String text) {
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

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void signInRequiredDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Wymagana autoryzacja")
                .setMessage("Zawartość tylko dla zarejstrowanych użytkowników. Czy chcesz się zalogować?")
                .setIcon(android.R.drawable.ic_secure)
                .setPositiveButton(R.string.login, (dialog, whichButton) -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> ((Activity) context).finish())
                .setCancelable(false)
                .show();
    }

    public static void showNeedInternetDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.missing_internet_connection)
                .setMessage(R.string.message_connect_to_internet_and_refresh)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.button_ok, (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

    public static void showRemoveSentenceAlert(Context context, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_title_removing)
                .setMessage(R.string.alert_message_removing_sentence)
                .setPositiveButton(R.string.button_ok, listener)
                .setNegativeButton(R.string.button_cancel, (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

    public static void sendBroadcastUserSentencesChanged(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_USER_SENTENCES_CHANGED);
        context.sendBroadcast(intent);
    }

    public static void enableDoneButtonForMultilineEditText(EditText editText) {
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    public static boolean validateSentenceText(String sentence) {
        return !sentence.trim().isEmpty();
    }

    public static boolean validateCommentText(String commentText) {
        return true;
    }
}
