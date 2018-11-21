package pl.jozefniemiec.langninja.ui.sentences;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.reader.OnReaderFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.reader.ReaderFragment;
import pl.jozefniemiec.langninja.ui.sentences.card.OnSentenceCardFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardFragment;
import pl.jozefniemiec.langninja.ui.speech.OnSpeechRecognitionFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerFragment;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceCardViewerActivity extends DaggerAppCompatActivity
        implements
        SentenceCardViewerContract.View,
        OnReaderFragmentInteractionListener,
        OnSpeechRecognitionFragmentInteractionListener,
        OnSentenceCardFragmentInteractionListener {

    private static final long ANIMATION_DELAY_MILIS = 500;

    @BindView(R.id.sentencePageCount)
    TextView sentencePageCountTextView;

    @BindView(R.id.sentenceCardViewerWithoutSpokenTextLayout)
    ConstraintLayout layout;

    @BindView(R.id.sentencePageAnswerTv)
    TextView sentencePageAnswerTextView;

    @Inject
    SentenceCardViewerContract.Presenter presenter;

    private SentenceCardFragment sentenceCard;
    private ReaderFragment reader;
    private SpeechRecognizerFragment speechRecognizer;
    private String languageCode;

    private ConstraintSet layoutNoSpokenTextSet = new ConstraintSet();
    private ConstraintSet layoutWithSpokenTextSet = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_without_spoken_text);
        ButterKnife.bind(this);
        languageCode = getIntent().getStringExtra(LANGUAGE_CODE_KEY);
        if (languageCode == null) {
            throw new RuntimeException(this.toString()
                    + ": language code missing in intent");
        }
        attachFragments();
        presenter.onViewCreated();
    }

    private void attachFragments() {
        reader = ReaderFragment.newInstance(languageCode);
        sentenceCard = SentenceCardFragment.newInstance(languageCode);
        speechRecognizer = SpeechRecognizerFragment.newInstance(languageCode);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sentenceCardFragmentContainer, sentenceCard)
                .add(R.id.readerFragmentContainer, reader)
                .add(R.id.speechRecognizerFragmentContainer, speechRecognizer)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutNoSpokenTextSet.clone(layout);
        layoutWithSpokenTextSet.clone(this, R.layout.activity_viewer_with_spoken_text);
    }

    @Override
    public String getCurrentSentence() {
        return sentenceCard.getCurrentSentence();
    }

    @Override
    public void onSpeechRecognizerResult(List<String> spokenTextsList) {
        presenter.onTextSpokenResult(spokenTextsList);
    }

    @Override
    public void onPageChange(int position, int pageCount) {
        presenter.onSentencePageChanged(position, pageCount);
    }

    @Override
    public void showCorrectSpokenText(String text) {
        changeTextViewBackground(sentencePageAnswerTextView, Color.GREEN);
        sentencePageAnswerTextView.setText(text);
        applyConstraintSetToLayout(layoutWithSpokenTextSet);
    }

    @Override
    public void showWrongSpokenText(String text) {
        changeTextViewBackground(sentencePageAnswerTextView, Color.RED);
        sentencePageAnswerTextView.setText(text);
        applyConstraintSetToLayout(layoutWithSpokenTextSet);
    }

    @Override
    public void hideSpokenText() {
        applyConstraintSetToLayout(layoutNoSpokenTextSet);
    }

    @Override
    public void updateNumbering(int position, int pageCount) {
        String pageCountFormat = getResources().getString(R.string.page_count);
        sentencePageCountTextView.setText(String.format(pageCountFormat, position, pageCount));
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
}
