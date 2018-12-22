package pl.jozefniemiec.langninja.data.resources;

public interface ResourcesManager {

    String getLanguageName(String languageCode);

    int getFlagId(String languageCode);

    String findOnSpeechErrorMessage(int errorCode);

    String getNeedInternetConnectionMessage();

    String getLanguageNotSupportedMessage();

    String getUnknownErrorMessage();
}
