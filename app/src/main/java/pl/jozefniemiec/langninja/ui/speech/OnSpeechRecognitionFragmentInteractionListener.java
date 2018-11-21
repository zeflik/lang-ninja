package pl.jozefniemiec.langninja.ui.speech;

import java.util.List;

public interface OnSpeechRecognitionFragmentInteractionListener {

    void onSpeechRecognizerResult(List<String> spokenTextsList);
}
