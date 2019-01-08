package pl.jozefniemiec.langninja.ui.speech;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import static pl.jozefniemiec.langninja.LangNinjaApplication.APP_PACKAGE_NAME;
import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SPEECH_RECOGNIZER_PACKAGE;

public class SpeechRecognizerFragment extends DaggerFragment implements SpeechRecognizerContract.View {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int SHOW_APPLICATION_SETTINGS_REQUEST_CODE = 2;
    private static final String MESSAGE_MUST_PASS_VALID_LANGUAGE_CODE = "%s must pass valid language code";
    private static final String URI_SCHEME = "package";
    private static final String MESSAGE_MUST_IMPLEMENT_INTERACTION_LISTENER =
            "%s must implement SpeechRecognitionFragmentInteractionListener";

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
            throw new RuntimeException(
                    String.format(MESSAGE_MUST_PASS_VALID_LANGUAGE_CODE, requireContext().toString())
            );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSpeechRecognitionFragmentInteractionListener) {
            listener = (OnSpeechRecognitionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    String.format(MESSAGE_MUST_IMPLEMENT_INTERACTION_LISTENER, context.toString())
            );
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
    }

    @Override
    public void activateSpeechRecognizer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)) {
            presenter.onSpeechRecognizerInsufficientPermission();
        } else {
            initializeSpeechRecognizer();
        }
    }

    private void initializeSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        boolean recognitionAvailable = SpeechRecognizer.isRecognitionAvailable(requireContext());
        presenter.onSpeechRecognizerInit(recognitionAvailable);
    }

    @Override
    public void askForSpeechPermissions() {
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSIONS_REQUEST_RECORD_AUDIO);
    }

    @Override
    public void showSpeechInsufficientPermissionButton() {
        speechButton.setOnClickListener(x -> presenter.speechRecognizerButtonClickedWithNoPermissions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onSpeechPermissionGranted();
                } else {
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED
                            && !shouldShowRequestPermissionRationale(permissions[0])) {
                        presenter.speechRecognizerButtonClickedWithNoPermissionsPermanently();
                    }
                }
            }
        }
    }

    @Override
    public void showInsufficientPermissionDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_insufficient_permissions)
                .setMessage(R.string.message_open_app_settings)
                .setPositiveButton(R.string.button_ok, (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts(URI_SCHEME, APP_PACKAGE_NAME, null);
                    intent.setData(uri);
                    startActivityForResult(intent, SHOW_APPLICATION_SETTINGS_REQUEST_CODE);
                })
                .setNegativeButton(R.string.button_cancel, (dialog, i) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void activateSpeechButton() {
        speechButton.setBackgroundResource(R.drawable.microphone);
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
            try {
                unHighlightSpeechButton();
            } catch (NullPointerException e) {
                Log.d(TAG, "cancelSpeechListening: " + e.getMessage());
            }
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
        return AppUtils.checkForApplication(requireContext(), SPEECH_RECOGNIZER_PACKAGE);
    }

    @Override
    public void findSpeechSupportedLanguages() {
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        detailsIntent.setPackage(SPEECH_RECOGNIZER_PACKAGE);
        requireActivity().sendOrderedBroadcast(
                detailsIntent, null, new SpeechBroadcastReceiver(presenter), null, Activity.RESULT_OK, null, null
        );
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
