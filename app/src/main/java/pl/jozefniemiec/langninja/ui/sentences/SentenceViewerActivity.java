package pl.jozefniemiec.langninja.ui.sentences;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.sentences.card.OnSentenceCardFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardFragment;
import pl.jozefniemiec.langninja.ui.sentences.reader.OnReaderFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.reader.ReaderFragment;
import pl.jozefniemiec.langninja.ui.sentences.speech.OnSpeechRecognitionFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.speech.SpeechRecognizerFragment;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceViewerActivity extends DaggerAppCompatActivity
        implements
        OnReaderFragmentInteractionListener,
        OnSpeechRecognitionFragmentInteractionListener,
        OnSentenceCardFragmentInteractionListener {

    @BindView(R.id.languagePageCount)
    TextView languagePageCountTextView;

    private SentenceCardFragment sentenceCard;
    private ReaderFragment reader;
    private SpeechRecognizerFragment speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ButterKnife.bind(this);
        attachFragments();
    }

    public void attachFragments() {
        String languageCode = getIntent().getStringExtra(LANGUAGE_CODE_KEY);
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
    public String getSentenceToRead() {
        return sentenceCard.getCurrentSentence();
    }

    @Override
    public void onSpeechRecognizerResult(List<String> spokenTextsList) {
        Toast.makeText(this, spokenTextsList.get(0), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChange(int position, int pageCount) {
        String pageCountFormat = getResources().getString(R.string.page_count);
        languagePageCountTextView.setText(String.format(pageCountFormat, position, pageCount));
    }

//    @Override
//    public void showCorrectSpokenText(String text) {
//        changeTextViewBackground(spokenTextTv, Color.GREEN);
//        spokenTextTv.setText(text);
//        applyConstraintSetToLayout(layoutWithAnswerSet);
//    }
//
//    @Override
//    public void showWrongSpokenText(String text) {
//        changeTextViewBackground(spokenTextTv, Color.RED);
//        spokenTextTv.setText(text);
//        applyConstraintSetToLayout(layoutWithAnswerSet);
//    }
//
//    @Override
//    public void hideSpokenText() {
//        applyConstraintSetToLayout(layoutNoAnswerSet);
//    }
//
//    private void applyConstraintSetToLayout(ConstraintSet constraintSet) {
//        new Handler().postDelayed(() -> {
//            TransitionManager.beginDelayedTransition(layout);
//            constraintSet.applyTo(layout);
//        }, ANIMATION_DELAY_MILIS);
//    }
//
//    private void changeTextViewBackground(TextView textView, int color) {
//        GradientDrawable tvBackground = (GradientDrawable) textView.getBackground();
//        tvBackground.setColor(color);
//    }
}
