package pl.jozefniemiec.langninja.resources;

import android.content.res.Resources;
import android.speech.SpeechRecognizer;

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

    @Override
    public String findSpeechErrorMessage(int errorCode) {
        int messageId;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                messageId = R.string.speech_error_audio;
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                messageId = R.string.speech_unknown_error;
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
                messageId = R.string.speech_error_server;
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
