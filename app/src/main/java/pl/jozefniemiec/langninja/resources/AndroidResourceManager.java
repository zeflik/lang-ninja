package pl.jozefniemiec.langninja.resources;

import android.content.res.Resources;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;

public class AndroidResourceManager implements ResourcesManager {

    private final Resources resources;
    private final String packageName;

    @Inject
    public AndroidResourceManager(Resources resources) {
        this.resources = resources;
        packageName = resources.getString(R.string.app_package_name);
    }

    @Override
    public String getLanguageName(String languageCode) {
        String resourceName = resources.getString(R.string.language_name_prefix) + languageCode;
        int id = resources.getIdentifier(resourceName, "string", packageName);
        return resources.getString(id);
    }

    @Override
    public int getFlagId(String languageCode) {
        return resources.getIdentifier(languageCode.toLowerCase(), "drawable", packageName);
    }
}
