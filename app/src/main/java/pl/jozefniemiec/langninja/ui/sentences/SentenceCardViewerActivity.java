package pl.jozefniemiec.langninja.ui.sentences;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.base.BaseActivity;
import pl.jozefniemiec.langninja.ui.base.Constants;
import pl.jozefniemiec.langninja.ui.editor.SentenceEditor;
import pl.jozefniemiec.langninja.ui.reader.OnReaderFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.reader.ReaderFragment;
import pl.jozefniemiec.langninja.ui.sentences.card.OnSentenceCardFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCardFragment;
import pl.jozefniemiec.langninja.ui.sentences.comments.CommentsFragment;
import pl.jozefniemiec.langninja.ui.sentences.community.CommunityCardFragment;
import pl.jozefniemiec.langninja.ui.sentences.community.OnCommunityCardFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.speech.OnSpeechRecognitionFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerFragment;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SentenceCardViewerActivity extends BaseActivity
        implements
        SentenceCardViewerContract.View,
        OnReaderFragmentInteractionListener,
        OnSpeechRecognitionFragmentInteractionListener,
        OnSentenceCardFragmentInteractionListener,
        OnCommunityCardFragmentInteractionListener {

    private static final String TAG = SentenceCardViewerActivity.class.getSimpleName();
    private static final String READER_TAG = "reader tag";
    private static final String SPEECH_TAG = "speech tag";
    private static final String SENTENCE_CARD_TAG = SentenceCardFragment.class.getSimpleName();
    public static final int EDIT_REQUEST_CODE = 1;
    public static final String COMMENTS_TAG = "comments tag";

    private SentenceCardFragment sentenceCard;
    private ReaderFragment reader;
    private SpeechRecognizerFragment speechRecognizer;
    private CommunityCardFragment communityCardFragment;
    private String languageCode;
    private String sentence;
    private String sentenceId;
    private MenuItem editMenuItem;
    private MenuItem deleteMenuItem;

    private ConstraintSet layoutNoSpokenTextSet = new ConstraintSet();
    private ConstraintSet layoutWithSpokenTextSet = new ConstraintSet();

    @BindView(R.id.sentencePageCount)
    TextView sentencePageCountTextView;

    @BindView(R.id.sentenceCardViewerWithoutSpokenTextLayout)
    ConstraintLayout layout;

    @BindView(R.id.sentencePageAnswerTv)
    TextView sentencePageAnswerTextView;

    @BindView(R.id.commentsContainer)
    FrameLayout commentsFrameLayout;

    @Inject
    SentenceCardViewerContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_without_spoken_text);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            languageCode = savedInstanceState.getString(LANGUAGE_CODE_KEY);
            sentence = savedInstanceState.getString(SENTENCE_KEY);
            findFragmentReferences();
        } else {
            languageCode = Objects.requireNonNull(getIntent().getStringExtra(LANGUAGE_CODE_KEY));
            sentence = getIntent().getStringExtra(SENTENCE_KEY);
            attachFragments();
        }
    }

    private void findFragmentReferences() {
        sentenceCard = (SentenceCardFragment)
                getSupportFragmentManager().findFragmentByTag(SENTENCE_CARD_TAG);
        reader = (ReaderFragment)
                getSupportFragmentManager().findFragmentByTag(READER_TAG);
        speechRecognizer = (SpeechRecognizerFragment)
                getSupportFragmentManager().findFragmentByTag(SPEECH_TAG);
    }

    private void attachFragments() {
        reader = ReaderFragment.newInstance(languageCode);
        sentenceCard = SentenceCardFragment.newInstance(languageCode, sentence);
        speechRecognizer = SpeechRecognizerFragment.newInstance(languageCode);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager
                .beginTransaction()
                .add(R.id.sentenceCardFragmentContainer, sentenceCard, SENTENCE_CARD_TAG)
                .add(R.id.readerFragmentContainer, reader, READER_TAG)
                .add(R.id.speechRecognizerFragmentContainer, speechRecognizer, SPEECH_TAG)
                .commit();

        sentenceId = getIntent().getStringExtra(SENTENCE_ID_KEY);
        if (sentenceId != null) {
            communityCardFragment = CommunityCardFragment.newInstance(sentenceId);
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.sentenceCommunityFragmentContainer, communityCardFragment)
                    .commit();
        }
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
    public void stopReading() {
        reader.stopReading();
    }

    @Override
    public void stopListening() {
        speechRecognizer.cancelSpeechListening();
    }

    @Override
    public void updateNumbering(int position, int pageCount) {
        String pageCountFormat = getResources().getString(R.string.page_count);
        sentencePageCountTextView.setText(String.format(pageCountFormat, position, pageCount));
    }

    @Override
    public void editSentence(String sentenceId) {
        Intent intent = new Intent(this, SentenceEditor.class);
        intent.putExtra(SENTENCE_ID_KEY, sentenceId);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    @Override
    public void notifyDataChanged() {
        Utility.sendBroadcastUserSentencesChanged(this);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showEditButtons() {
        editMenuItem.setVisible(true);
        deleteMenuItem.setVisible(true);
    }

    @Override
    public void hideEditButtons() {
        editMenuItem.setVisible(false);
        deleteMenuItem.setVisible(false);
    }

    @Override
    public void showRemoveSentenceAlert() {
        Utility.showRemoveSentenceAlert(this, (dialog, whichButton) -> presenter.removeSentence(sentenceId));
    }

    private void applyConstraintSetToLayout(ConstraintSet constraintSet) {
        new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(layout);
            constraintSet.applyTo(layout);
        }, Constants.ANIMATION_DELAY_MILIS);
    }

    private void changeTextViewBackground(TextView textView, int color) {
        GradientDrawable tvBackground = (GradientDrawable) textView.getBackground();
        tvBackground.setColor(color);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LANGUAGE_CODE_KEY, languageCode);
        outState.putString(SENTENCE_KEY, sentence);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            sentenceCard.setCurrentSentence(data.getStringExtra(SENTENCE_KEY));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sentence_menu, menu);
        editMenuItem = menu.findItem(R.id.menu_sentence_edit);
        deleteMenuItem = menu.findItem(R.id.menu_sentence_delete);
        presenter.onMenuCreated(sentenceId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sentence_edit:
                presenter.onSentenceEditButtonClicked(sentenceId);
                break;
            case R.id.menu_sentence_delete:
                presenter.onSentenceRemoveButtonClicked();
                break;
            case R.id.menu_sentence_feedback:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCommentsButtonPressed() {
        presenter.onCommentsButtonPressed();
    }

    @Override
    public void showComments() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment commentsFragment = supportFragmentManager.findFragmentByTag(COMMENTS_TAG);
        if (commentsFragment == null) {
            commentsFragment = CommentsFragment.newInstance(sentenceId);
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.commentsContainer, commentsFragment, COMMENTS_TAG)
                    .commit();
        }
    }
}
