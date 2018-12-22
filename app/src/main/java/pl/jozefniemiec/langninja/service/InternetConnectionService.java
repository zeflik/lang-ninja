package pl.jozefniemiec.langninja.service;

import android.content.Context;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.utils.Utility;

public class InternetConnectionService {

    private final Context appContext;

    @Inject
    InternetConnectionService(Context appContext) {
        this.appContext = appContext;
    }

    public Boolean isInternetOn() {

        return Utility.isNetworkAvailable(appContext);
    }
}
