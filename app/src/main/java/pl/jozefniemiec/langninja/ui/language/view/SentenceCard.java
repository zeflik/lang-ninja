package pl.jozefniemiec.langninja.ui.language.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;
import pl.jozefniemiec.langninja.ui.language.view.listener.speech.SpeechRecognitionListener;

import static pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment.LANGUAGE_CODE;

public class SentenceCard extends DaggerAppCompatActivity
        implements
        SentenceCardView,
        ViewPager.OnPageChangeListener {

    public static final int HIGHLIGHT_BUTTON_COLOR = Color.GREEN;
    public static final int INACTIVE_BUTTON_COLOR = Color.LTGRAY;
    private static final String TAG = "SentenceCard";

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardPresenter presenter;

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

    @BindView(R.id.language_card_play_button)
    ImageButton readButton;

    @BindView(R.id.language_card_microphone_button)
    ImageButton speechButton;

    @BindView(R.id.languagePageAnswerTv)
    TextView answerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_card);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(getIntent().getStringExtra(LANGUAGE_CODE));

        presenter.loadData(getIntent().getStringExtra(LANGUAGE_CODE));
        textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
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
    public void showNumbering(String numbering) {
        numberingTv.setText(numbering);
    }

    @Override
    public void activateReadButton() {
        readButton.setOnClickListener(x -> presenter.playButtonClicked());
        showButton(readButton);
    }

    @Override
    public void deactivateReadButton() {
        readButton.setOnClickListener(x -> presenter.deactivatedPlayButtonClicked());
        hideButton(readButton);
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

    @Override
    public void activateSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        boolean recognitionAvailable = SpeechRecognizer.isRecognitionAvailable(this);
        presenter.onSpeechRecognizerInit(recognitionAvailable);
    }

    @Override
    public void activateSpeechButton() {
        unHighlightSpeechButton();
        speechButton.setOnClickListener(x -> presenter.unHighlightedMicrophoneButtonClicked());
    }

    @Override
    public void deactivateSpeechButton() {
        hideButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.deactivatedMicrophoneButtonClicked());
    }

    @Override
    public void highlightSpeechButton() {
        highlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.highlightedMicrophoneButtonClicked());
    }

    @Override
    public void unHighlightSpeechButton() {
        unHighlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.unHighlightedMicrophoneButtonClicked());
    }

    @Override
    public void startListening(String languageCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        speechRecognizer.startListening(intent);
    }

    @Override
    public boolean isListeningSpeech() {
        return speechRecognitionListener.isListeningSpeech();
    }

    @Override
    public void showSpokenText(String text) {
        answerTv.setText(text);
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

    private void hideButton(ImageButton imageButton) {
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
}
