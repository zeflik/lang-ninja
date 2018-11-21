package pl.jozefniemiec.langninja.ui.sentences.speech;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.utils.AppUtils;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SpeechRecognizerFragment extends DaggerFragment implements SpeechRecognizerContract.View {

    private static String TAG = SpeechRecognizerFragment.class.getSimpleName();

    @Inject
    SpeechRecognizerContract.Presenter presenter;

    @Inject
    SpeechRecognizer speechRecognizer;

    @Inject
    SpeechRecognitionListener speechRecognitionListener;

    @BindView(R.id.speechButton)
    ImageButton speechButton;

    private OnSpeechRecognitionFragmentInteractionListener listener;
    private Unbinder unbinder;
    private String languageCode;

    public SpeechRecognizerFragment() {
    }

    public static SpeechRecognizerFragment newInstance(String languageCode) {
        SpeechRecognizerFragment fragment = new SpeechRecognizerFragment();
        Bundle args = new Bundle();
        args.putString(LANGUAGE_CODE_KEY, languageCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(LANGUAGE_CODE_KEY)) {
            languageCode = getArguments().getString(LANGUAGE_CODE_KEY);
        } else {
            throw new RuntimeException(requireContext().toString()
                    + " must pass valid language code");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSpeechRecognitionFragmentInteractionListener) {
            listener = (OnSpeechRecognitionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SpeechRecognitionFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speech_recognizer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated(languageCode);
        activateSpeechRecognizer();
    }

    public void activateSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        boolean recognitionAvailable = SpeechRecognizer.isRecognitionAvailable(requireContext());
        presenter.onSpeechRecognizerInit(recognitionAvailable);
    }

    @Override
    public void activateSpeechButton() {
        speechButton.setOnClickListener(x -> presenter.speechRecognizerButtonClicked());
    }

    @Override
    public void deactivateSpeechButton() {
        Utility.grayOutButton(requireActivity(), speechButton);
        speechButton.setOnClickListener(x -> presenter.deactivatedSpeechButtonClicked());
    }

    @Override
    public void highlightSpeechButton() {
        Utility.highlightButton(requireActivity(), speechButton);
        speechButton.setOnClickListener(x -> presenter.highlightedSpeechButtonClicked());
    }

    @Override
    public void unHighlightSpeechButton() {
        Utility.unHighlightButton(requireActivity(), speechButton);
        speechButton.setOnClickListener(x -> presenter.speechRecognizerButtonClicked());
    }

    @Override
    public void startListening(String languageCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireActivity().getPackageName());
        speechRecognizer.startListening(intent);
    }

    @Override
    public boolean isListeningSpeech() {
        return speechRecognitionListener.isListeningSpeech();
    }


    @Override
    public void stopSpeechListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    @Override
    public void cancelSpeechListening() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
        }
    }

    @Override
    public void showSpeechRecognizerInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.agree_to_install_google)
                .setPositiveButton(R.string.button_ok, (dialog, id) ->
                        AppUtils.openPlayStoreForSpeechRecognizer(requireActivity())
                )
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            super.onDestroyView();
        }
    }

    @Override
    public boolean isSpeechRecognizerAvailable() {
        return AppUtils.checkForApplication(
                requireContext(),
                getString(R.string.google_speech_package_name)
        );
    }

    @Override
    public void findSpeechSupportedLanguages() {
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        requireActivity().sendOrderedBroadcast(
                detailsIntent, null, new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle results = getResultExtras(true);
                        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                            presenter.onSpeechSupportedLanguages(
                                    results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
                            );
                        }
                    }
                }, null, Activity.RESULT_OK, null, null);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSpeechResult(ArrayList<String> spokenTextsList) {
        listener.onSpeechRecognizerResult(spokenTextsList);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
