package pl.jozefniemiec.langninja.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import javax.inject.Inject;

public class ApplicationsManager {

    public static final int INSTALLED_ENABLED = 0;
    public static final int INSTALLED_DISABLED = -1;
    public static final int NOT_INSTALLED = -2;

    private final Context context;

    @Inject
    public ApplicationsManager(Context context) {
        this.context = context;
    }

    public int checkForApplication(String packageName) {
        int flag = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, flag);
            return (appInfo.enabled) ? INSTALLED_ENABLED : INSTALLED_DISABLED;
        } catch (PackageManager.NameNotFoundException e) {
            return NOT_INSTALLED;
        }
    }
}
