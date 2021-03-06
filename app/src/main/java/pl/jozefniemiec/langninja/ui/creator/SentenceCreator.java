package pl.jozefniemiec.langninja.ui.creator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.base.BaseSecuredActivity;
import pl.jozefniemiec.langninja.ui.base.spinner.LanguagesSpinnerAdapter;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SentenceCreator extends BaseSecuredActivity
        implements SentenceCreatorContract.View {

    public static final String USER_SENTENCE_TAG = "user_sentence_tag";
    private static final String TAG = SentenceCreator.class.getSimpleName();
    private InputMethodManager imm;

    @BindView(R.id.sentenceCandidateTextInput)
    EditText sentenceTextInput;

    @BindView(R.id.sentenceCandidateSendButton)
    Button sentenceCreateButton;

    @BindView(R.id.newSentenceProgressBar)
    ProgressBar progressBar;

    @OnClick(R.id.sentenceCandidateSendButton)
    public void submit(View view) {
        presenter.onCreateButtonClicked(
                (Language) languagesSpinner.getSelectedItem(),
                sentenceTextInput.getText().toString()
        );
    }

    @BindView(R.id.sentenceCandidateTestButton)
    Button sentenceTestButton;

    @OnClick(R.id.sentenceCandidateTestButton)
    public void test(View view) {
        presenter.onTestButtonClicked(
                (Language) languagesSpinner.getSelectedItem(),
                sentenceTextInput.getText().toString()
        );
    }

    @BindView(R.id.languages_spinner)
    Spinner languagesSpinner;

    @Inject
    SentenceCreatorContract.Presenter presenter;

    @Inject
    LanguagesSpinnerAdapter languagesSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_creator);
        ButterKnife.bind(this);
        String languageCode = null;
        if (savedInstanceState == null) {
            languageCode = getIntent().getStringExtra(LANGUAGE_CODE_KEY);
        }
        Utility.enableDoneButtonForMultilineEditText(sentenceTextInput);
        presenter.onViewCreated(languageCode);
    }

    @Override
    public void initializeSpinner(List<Language> languages) {
        languagesSpinnerAdapter.addAll(languages);
        languagesSpinner.setAdapter(languagesSpinnerAdapter);
    }

    @Override
    public void selectSpinnerAtPosition(int position) {
        languagesSpinner.setSelection(position);
    }


    @Override
    public void onResume() {
        super.onResume();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void showSentenceCard(String langCode, String sentence) {
        Intent intent = new Intent(this, SentenceCardViewerActivity.class);
        intent.putExtra(LANGUAGE_CODE_KEY, langCode);
        intent.putExtra(SENTENCE_KEY, sentence);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showKeyboard() {
        sentenceTextInput.requestFocus();
        imm.showSoftInput(sentenceTextInput, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void hideKeyboard() {
        imm.hideSoftInputFromWindow(sentenceTextInput.getWindowToken(), 0);
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
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void showNeedInternetInfo() {
        Toast.makeText(this, R.string.missing_internet_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                             WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);
    }
}
