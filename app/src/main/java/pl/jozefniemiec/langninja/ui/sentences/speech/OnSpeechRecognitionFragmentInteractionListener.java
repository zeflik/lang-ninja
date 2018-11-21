package pl.jozefniemiec.langninja.ui.sentences.speech;

import java.util.List;

public interface OnSpeechRecognitionFragmentInteractionListener {

    void onSpeechRecognizerResult(List<String> spokenTextsList);
}
