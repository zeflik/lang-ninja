package pl.jozefniemiec.langninja.data.resources;

import android.content.res.Resources;
import android.speech.SpeechRecognizer;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.R;

public class AndroidResourceManager implements ResourcesManager {

    private static final String TAG = AndroidResourceManager.class.getSimpleName();
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "%s %s - resource not found.";
    private static final String RESOURCE_TYPE_STRING = "string";
    private static final String RESOURCE_TYPE_DRAWABLE = "drawable";
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
        int id = findResourceId(resourceName, RESOURCE_TYPE_STRING, packageName);
        return resources.getString(id);
    }

    @Override
    public int getFlagId(String languageCode) {
        return findResourceId(languageCode.toLowerCase(), RESOURCE_TYPE_DRAWABLE, packageName);
    }

    private int findResourceId(String resourceName, String resourceType, String packageName) {
        int id = resources.getIdentifier(resourceName, resourceType, packageName);
        if (id == 0) {
            throw new RuntimeException(String.format(RESOURCE_NOT_FOUND_MESSAGE, resourceName, resourceType));
        }
        return id;
    }

    @Override
    public String findOnSpeechErrorMessage(int errorCode) {
        int messageId;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                messageId = R.string.speech_error_audio;
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                messageId = R.string.speech_error_client;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                messageId = R.string.speech_error_permission;
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                messageId = R.string.speech_error_network;
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                messageId = R.string.speech_error_no_input;
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                messageId = R.string.speech_error_no_match;
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                messageId = R.string.speech_error_busy;
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                messageId = R.string.speech_error_no_input;
                break;
            case SpeechRecognizer.ERROR_SERVER:
                messageId = R.string.need_internet_connection;
                break;
            default:
                messageId = R.string.speech_unknown_error;
        }
        return resources.getString(messageId);
    }

    @Override
    public String getNeedInternetConnectionMessage() {
        return resources.getString(R.string.need_internet_connection);
    }

    @Override
    public String getLanguageNotSupportedMessage() {
        return resources.getString(R.string.language_not_supported);
    }
}
