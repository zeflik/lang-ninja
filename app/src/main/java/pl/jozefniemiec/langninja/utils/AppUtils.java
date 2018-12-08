package pl.jozefniemiec.langninja.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import pl.jozefniemiec.langninja.R;

public final class AppUtils {

    public static void openPlayStoreForSpeechRecognizer(Context context) {
        openPlayStoreForApp(context, context.getResources().getString(R.string.google_speech_package_name));
    }

    public static void openPlayStoreForTTS(Context context) {
        openPlayStoreForApp(context, context.getResources().getString(R.string.google_tts_package_name));
    }

    private static void openPlayStoreForApp(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_market_link) + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_google_play_store_link) + appPackageName)));
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
