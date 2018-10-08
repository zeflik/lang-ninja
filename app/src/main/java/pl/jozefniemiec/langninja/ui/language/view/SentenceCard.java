package pl.jozefniemiec.langninja.ui.language.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;

import static pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment.LANGUAGE_CODE;

public class SentenceCard extends DaggerAppCompatActivity
        implements
        SentenceCardView,
        ViewPager.OnPageChangeListener,
        RecognitionListener {

    public static final int HIGHLIGHT_COLOR_FILTER = Color.GREEN;

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardPresenter presenter;

    @Inject
    SpeechRecognizer speechRecognizer;

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
        presenter.speechAvailable(SpeechRecognizer.isRecognitionAvailable(this));
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
    public void showReadButton() {
        readButton.setOnClickListener(x -> presenter.playButtonClicked());
        showButton(readButton);
    }

    @Override
    public void hideReadButton() {
        readButton.setOnClickListener(x -> presenter.hiddenPlayButtonClicked());
        hideButton(readButton);
    }

    @Override
    public void activateSpeechButton() {
        speechRecognizer.setRecognitionListener(this);
        unHighlightSpeakButton();
    }

    @Override
    public void highlightSpeakButton() {
        highlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.highlightedMicrophoneButtonClicked());
    }

    @Override
    public void unHighlightSpeakButton() {
        unHighlightButton(speechButton);
        speechButton.setOnClickListener(x -> presenter.unHighlightedMicrophoneButtonClicked());
    }

    @Override
    public void deactivateSpeechButton() {
        speechButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        speechButton.setOnClickListener(x -> presenter.deactivatedMicrophoneButtonClicked());
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
    public void listenSpeech(String languageCode) {
        highlightSpeakButton();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechRecognizer.startListening(intent);
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
    public void showSpokenText(String text) {
        answerTv.setText(text);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        presenter.speechListening();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        presenter.speechEnded();
    }

    @Override
    public void onError(int errorCode) {
        presenter.speechError(errorCode);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> spokenTextsList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        presenter.spokenText(spokenTextsList);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

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
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    private void hideButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP));
    }

    private void showButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
    }

    private void highlightButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().setColorFilter(HIGHLIGHT_COLOR_FILTER, PorterDuff.Mode.SRC_ATOP));
    }

    private void unHighlightButton(ImageButton imageButton) {
        runOnUiThread(() -> imageButton.getBackground().clearColorFilter());
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
        super.onDestroy();
    }
}
