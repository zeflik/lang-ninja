package pl.jozefniemiec.langninja.resources;

public interface ResourcesManager {

    String getLanguageName(String languageCode);

    int getFlagId(String s);

    String findOnSpeechErrorMessage(int errorCode);

    String getNeedInternetConnectionMessage();

    String getLanguageNotSupportedMessage();
}
