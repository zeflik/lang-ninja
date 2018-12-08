package pl.jozefniemiec.langninja.ui.creator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.base.BaseSecuredActivity;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SentenceCreator extends BaseSecuredActivity
        implements SentenceCreatorContract.View {

    @BindView(R.id.sentenceCandidateTextInput)
    EditText sentenceTextInput;

    @BindView(R.id.sentenceCandidateSendButton)
    Button sentenceCreateButton;

    @BindView(R.id.sentenceCandidateTestButton)
    Button sentenceTestButton;

    @BindView(R.id.languages_spinner)
    Spinner languagesSpinner;

    @Inject
    SentenceCreatorContract.Presenter presenter;

    SpinnerAdapter spinnerAdapter;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sentence);
        ButterKnife.bind(this);
        enableDoneButtonForMultilineEditText();
        attachListeners();
        presenter.onViewCreated();
    }

    @Override
    public void initializeSpinner(List<Language> languages) {
        spinnerAdapter = new SpinnerAdapter(this);
        spinnerAdapter.addAll(languages);
        languagesSpinner.setAdapter(spinnerAdapter);
    }

    private void enableDoneButtonForMultilineEditText() {
        sentenceTextInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sentenceTextInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void attachListeners() {
        sentenceCreateButton.setOnClickListener(v ->
                presenter.onCreateButtonClicked(
                        (Language) languagesSpinner.getSelectedItem(),
                        sentenceTextInput.getText().toString()
                )
        );
        sentenceTestButton.setOnClickListener(v ->
                presenter.onTestButtonClicked(
                        (Language) languagesSpinner.getSelectedItem(),
                        sentenceTextInput.getText().toString()
                )
        );
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
    public void close() {
        try {
            hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }
}
