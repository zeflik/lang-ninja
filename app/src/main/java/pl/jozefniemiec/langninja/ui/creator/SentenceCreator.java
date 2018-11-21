package pl.jozefniemiec.langninja.ui.creator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListFragment;
import pl.jozefniemiec.langninja.ui.creator.languageslist.LanguagesListListener;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceCreator extends DaggerAppCompatActivity
        implements SentenceCreatorContract.View, LanguagesListListener {

    @BindView(R.id.sentenceCandidateLangNameTextView)
    TextView langNameTextView;

    @BindView(R.id.sentenceCandidateTextInput)
    EditText sentenceTextInput;

    @BindView(R.id.sentenceCandidateSendButton)
    Button sentenceCreateButton;

    @BindView(R.id.sentenceCandidateTestButton)
    Button sentenceTestButton;

    @BindView(R.id.sentenceCandidateFlag)
    ImageButton flagImageButton;

    @BindView(R.id.sentenceCandidateFragmentContainer)
    FrameLayout frameLayout;

    @Inject
    SentenceCreatorContract.Presenter presenter;

    private String langCode;
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

    private void enableDoneButtonForMultilineEditText() {
        sentenceTextInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sentenceTextInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void attachListeners() {
        flagImageButton.setOnClickListener(v -> presenter.onFlagImageButtonClicked());
        sentenceCreateButton.setOnClickListener(v ->
                presenter.onCreateButtonClicked(langCode, sentenceTextInput.getText().toString())
        );
        sentenceTestButton.setOnClickListener(v ->
                presenter.onTestButtonClicked(langCode, sentenceTextInput.getText().toString())
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void showLanguagesListWindow() {
        frameLayout.setVisibility(View.VISIBLE);
        startLanguagesListForResult();
    }

    public void startLanguagesListForResult() {
        LanguagesListFragment languagesFragment = LanguagesListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sentenceCandidateFragmentContainer, languagesFragment)
                .addToBackStack(this.getClass().getSimpleName())
                .commit();
    }

    public void hideLanguagesListWindow() {
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLanguagesListResult(Language language) {
        presenter.onLanguagePicked(language);
    }

    @Override
    public void showLanguageData(Language language) {
        langCode = language.getCode();
        setLangName(language.getNativeName());
        setFlag(Utility.getLanguageFlagUri(this, langCode));
    }

    @Override
    public void showSentenceCard(String langCode, String sentence) {
        Intent intent = new Intent(this, SentenceCardViewerActivity.class);
        intent.putExtra(LANGUAGE_CODE_KEY, langCode);
        startActivity(intent);
    }

    private void setLangName(String name) {
        langNameTextView.setText(name);
    }

    private void setFlag(Uri uri) {
        Picasso
                .with(this)
                .load(uri)
                .centerCrop()
                .resize(flagImageButton.getMeasuredWidth(), flagImageButton.getMeasuredHeight())
                .into(flagImageButton);
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
        hideKeyboard();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideLanguagesListWindow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }
}
