package pl.jozefniemiec.langninja.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import static pl.jozefniemiec.langninja.ui.base.Constants.APP_MARKET_LINK;
import static pl.jozefniemiec.langninja.ui.base.Constants.PLAY_STORE_PATH;
import static pl.jozefniemiec.langninja.ui.base.Constants.READER_PACKAGE;
import static pl.jozefniemiec.langninja.ui.base.Constants.SPEECH_RECOGNIZER_PACKAGE;

public final class AppUtils {

    public static void openPlayStoreForSpeechRecognizer(Context context) {
        openPlayStoreForApp(context, SPEECH_RECOGNIZER_PACKAGE);
    }

    public static void openPlayStoreForTTS(Context context) {
        openPlayStoreForApp(context, READER_PACKAGE);
    }

    private static void openPlayStoreForApp(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_MARKET_LINK + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_PATH)));
        }
    }

    public static boolean checkForApplication(Context context, String packageName) {
        int flag = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, flag);
            return appInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
