package pl.jozefniemiec.langninja.ui.reader;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.utils.AppUtils;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.READER_PACKAGE;

public class ReaderFragment extends DaggerFragment implements ReaderContract.View {

    @Inject
    TextToSpeech textToSpeech;

    @Inject
    UtteranceProgressListener utteranceProgressListener;

    @Inject
    ReaderContract.Presenter presenter;

    @BindView(R.id.language_card_read_button)
    ImageButton readButton;

    @BindView(R.id.readerProgressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private String languageCode;
    private OnReaderFragmentInteractionListener onReaderFragmentInteractionListener;

    public ReaderFragment() {
    }

    public static ReaderFragment newInstance(String languageCode) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle args = new Bundle();
        args.putString(LANGUAGE_CODE_KEY, languageCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReaderFragmentInteractionListener) {
            onReaderFragmentInteractionListener = (OnReaderFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
        presenter.onViewCreated(languageCode);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroyView();
    }

    @Override
    public void showTTSInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.agree_to_install_tts)
                .setPositiveButton(R.string.button_ok, (dialog, id) ->
                        AppUtils.openPlayStoreForTTS(requireActivity())
                )
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public String getCurrentSentence() {
        return onReaderFragmentInteractionListener.getCurrentSentence();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> progressBar.setVisibility(View.GONE));
    }

    @Override
    public boolean isReaderAvailable() {
        return AppUtils.checkForApplication(requireContext(), READER_PACKAGE);
    }

    @Override
    public void activateReadButton() {
        readButton.setOnClickListener(x -> presenter.readButtonClicked());
        Utility.showButton(requireActivity(), readButton);
    }

    @Override
    public void deactivateReadButton() {
        readButton.setOnClickListener(x -> presenter.deactivatedReadButtonClicked());
        Utility.grayOutButton(requireActivity(), readButton);
    }

    @Override
    public void highlightReadButton() {
        Utility.highlightButton(requireActivity(), readButton);
    }

    @Override
    public void unHighlightReadButton() {
        Utility.unHighlightButton(requireActivity(), readButton);
    }

    @Override
    public void showErrorMessage(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean setReaderLanguage(Locale locale) {
        if (textToSpeech.isLanguageAvailable(locale) != TextToSpeech.LANG_NOT_SUPPORTED) {
            textToSpeech.setLanguage(locale);
            return true;
        }
        return false;
    }

    @Override
    public void read(String sentence) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, sentence);
        textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, params);
    }

    public boolean isReading() {
        return textToSpeech.isSpeaking();
    }

    @Override
    public void stopReading() {
        if (isReading()) {
            unHighlightReadButton();
            textToSpeech.stop();
        }
    }
}
