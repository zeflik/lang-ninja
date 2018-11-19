package pl.jozefniemiec.langninja.ui.sentences.card;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognitionListener;
import pl.jozefniemiec.langninja.utils.AppUtils;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceCard extends DaggerFragment
        implements
        SentenceCardContract.View,
        ViewPager.OnPageChangeListener {

    private static final String TAG = "SentenceCard";
    private static final long ANIMATION_DELAY_MILIS = 500;

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardContract.Presenter presenter;

    @Inject
    SpeechRecognizer speechRecognizer;

    @Inject
    SpeechRecognitionListener speechRecognitionListener;

    @BindView(R.id.language_card_view_pager)
    ViewPager viewPager;

    @BindView(R.id.languagePageCount)
    TextView numberingTv;

    @BindView(R.id.language_card_speech_button)
    ImageButton speechButton;

    @BindView(R.id.language_layout_without_spoken_text)
    ConstraintLayout layout;

    @BindView(R.id.languagePageAnswerTv)
    TextView spokenTextTv;

    private ConstraintSet layoutNoAnswerSet = new ConstraintSet();
    private ConstraintSet layoutWithAnswerSet = new ConstraintSet();
    private Unbinder unbinder;

    public static SentenceCard newInstance(String languageCode) {
        SentenceCard fragment = new SentenceCard();
        Bundle args = new Bundle();
        args.putString(LANGUAGE_CODE_KEY, languageCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.activity_sentence_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadData(getArguments().getString(LANGUAGE_CODE_KEY));
        activateSpeechRecognizer();
        layoutNoAnswerSet.clone(layout);
        layoutWithAnswerSet.clone(requireContext(), R.layout.activity_sentence_card_w_spoken_text);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.pageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showData() {
        viewPager.setAdapter(languagePageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void showNumbering(int currentPage, int pageCount) {
        String pageCountFormat = getResources().getString(R.string.page_count);
        numberingTv.setText(String.format(pageCountFormat, currentPage, pageCount));
    }

    @Override
    public boolean isSpeechRecognizerAvailable() {
        return AppUtils.checkForApplication(
                requireContext(),
                getString(R.string.google_speech_package_name)
        );
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
    public void showCorrectSpokenText(String text) {
        changeTextViewBackground(spokenTextTv, Color.GREEN);
        spokenTextTv.setText(text);
        applyConstraintSetToLayout(layoutWithAnswerSet);
    }

    @Override
    public void showWrongSpokenText(String text) {
        changeTextViewBackground(spokenTextTv, Color.RED);
        spokenTextTv.setText(text);
        applyConstraintSetToLayout(layoutWithAnswerSet);
    }

    @Override
    public void hideSpokenText() {
        applyConstraintSetToLayout(layoutNoAnswerSet);
    }

    private void applyConstraintSetToLayout(ConstraintSet constraintSet) {
        new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(layout);
            constraintSet.applyTo(layout);
        }, ANIMATION_DELAY_MILIS);
    }

    private void changeTextViewBackground(TextView textView, int color) {
        GradientDrawable tvBackground = (GradientDrawable) textView.getBackground();
        tvBackground.setColor(color);
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
    public void showErrorMessage(String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
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
    public void onPause() {
        presenter.onViewPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        presenter.onViewDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroyView();
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
                            presenter.onSpeechSupportedLanguages(results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES));
                        }
                    }
                }, null, Activity.RESULT_OK, null, null);
    }

    public String getCurrentSentence() {
        return presenter.getCurrentSentence();
    }
}