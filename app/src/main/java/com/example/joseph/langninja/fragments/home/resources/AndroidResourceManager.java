package com.example.joseph.langninja.fragments.home.resources;

import android.content.res.Resources;

import com.example.joseph.langninja.R;

public class AndroidResourceManager implements ResourcesManager {

    private final Resources resources;

    public AndroidResourceManager(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getLanguageName(String languageCode) {
        String resourceName = resources.getString(R.string.language_name_prefix) + languageCode;
        String packageName = resources.getString(R.string.app_package_name);
        int id = resources.getIdentifier(resourceName, "string", packageName);
        return resources.getString(id);
    }
}
