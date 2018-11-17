package pl.jozefniemiec.langninja.ui.sentences;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognitionListener;
import pl.jozefniemiec.langninja.utils.AppUtils;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceCard extends DaggerAppCompatActivity
        implements
        SentenceCardContract.View,
        ViewPager.OnPageChangeListener {

    public static final int HIGHLIGHT_BUTTON_COLOR = Color.GREEN;
    public static final int INACTIVE_BUTTON_COLOR = Color.LTGRAY;
    private static final String TAG = "SentenceCard";
    private static final long ANIMATION_DELAY_MILIS = 500;

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardContract.Presenter presenter;

    @Inject
    TextToSpeech textToSpeech;

    @Inject
    UtteranceProgressListener utteranceProgressListener;

    @Inject
    SpeechRecognizer speechRecognizer;

    @Inject
    SpeechRecognitionListener speechRecognitionListener;

    @BindView(R.id.language_card_view_pager)
    ViewPager viewPager;

    @BindView(R.id.languagePageCount)
    TextView numberingTv;

    @BindView(R.id.language_card_read_button)
    ImageButton readButton;

    @BindView(R.id.language_card_speech_button)
    ImageButton speechButton;

    @BindView(R.id.language_layout_without_spoken_text)
    ConstraintLayout layout;

    @BindView(R.id.languagePageAnswerTv)
    TextView spokenTextTv;

    private ConstraintSet layoutNoAnswerSet = new ConstraintSet();
    private ConstraintSet layoutWithAnswerSet = new ConstraintSet();
    private ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_card);
        ButterKnife.bind(this);

        supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        activateSpeechRecognizer();
        textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);

        layoutNoAnswerSet.clone(layout);
        layoutWithAnswerSet.clone(this, R.layout.activity_sentence_card_w_spoken_text);

        presenter.loadData(getIntent().getStringExtra(LANGUAGE_CODE_KEY));
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
    public void setTitle(String string) {
        supportActionBar.setTitle(string);
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
    public void activateReadButton() {
        readButton.setOnClickListener(x -> presenter.readButtonClicked());
        showButton(readButton);
    }

    @Override
    public void deactivateReadButton() {
        readButton.setOnClickListener(x -> presenter.deactivatedReadButtonClicked());
        grayOutButton(readButton);
    }

    @Override
    public void highlightReadButton() {
        highlightButton(readButton);
    }

    @Override
    public void unHighlightReadButton() {
        unHighlightButton(readButton);
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

    @Override
    public boolean isReading() {
        return textToSpeech.isSpeaking();
    }

    @Override
    public void stopReading() {
        textToSpeech.stop();
    }

    public void activateSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        boolean recognitionAvailable = SpeechRecognizer.isRecognitionAvailable(this);
        presenter.onSpeechRecognizerInit(recognitionAvailable);
    }

    @Override
    public void activateSpeechButton() {
        speechButton.setOnClickListener(x -> presenter.speechRecognizerButtonClicked());
    }

    @Override
    public void deactivateSpeechButton() {
        grayOutButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.deactivatedSpeechButtonClicked());
    }

    @Override
    public void highlightSpeechButton() {
        highlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.highlightedSpeechButtonClicked());
    }

    @Override
    public void unHighlightSpeechButton() {
        unHighlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.speechRecognizerButtonClicked());
    }

    @Override
    public void startListening(String languageCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
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
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showSpeechRecognizerInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.agree_to_install_google)
                .setPositiveButton(R.string.button_ok, (dialog, id) ->
                        AppUtils.openPlayStoreForSpeechRecognizer(this)
                )
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public void showTTSInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.agree_to_install_tts)
                .setPositiveButton(R.string.button_ok, (dialog, id) ->
                        AppUtils.openPlayStoreForTTS(this)
                )
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public boolean isReaderAvailable() {
        return AppUtils.checkForApplication(
                getApplicationContext(),
                getString(R.string.google_tts_package_name)
        );
    }

    @Override
    public boolean isSpeechRecognizerAvailable() {
        return AppUtils.checkForApplication(
                getApplicationContext(),
                getString(R.string.google_speech_package_name)
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        presenter.onViewPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onViewDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void grayOutButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().setColorFilter(INACTIVE_BUTTON_COLOR, PorterDuff.Mode.SRC_ATOP));
    }

    private void showButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
    }

    private void highlightButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().setColorFilter(HIGHLIGHT_BUTTON_COLOR, PorterDuff.Mode.SRC_ATOP));
    }

    private void unHighlightButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
    }

    @Override
    public void findSpeechSupportedLanguages() {
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(
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
}