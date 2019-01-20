package pl.jozefniemiec.langninja.ui.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.base.BaseSecuredActivity;
import pl.jozefniemiec.langninja.utils.Utility;

import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SentenceEditor extends BaseSecuredActivity implements SentenceEditorContract.View {

    private String sentenceId;

    @Inject
    SentenceEditorContract.Presenter presenter;

    @Inject
    Picasso picasso;

    @BindView(R.id.sentenceEditorFlagImageView)
    ImageView flagImageView;

    @BindView(R.id.sentenceEditorEditText)
    EditText sentenceEditText;

    @BindView(R.id.sentenceEditorSaveButton)
    Button saveButton;

    @BindView(R.id.sentenceEditorProgressBar)
    ProgressBar progressBar;

    @OnClick(R.id.sentenceEditorSaveButton)
    void onSaveButtonClicked(View view) {
        presenter.onSaveButtonClicked(sentenceEditText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_editor);
        ButterKnife.bind(this);
        sentenceId = getIntent().getStringExtra(SENTENCE_ID_KEY);
        presenter.loadData(sentenceId);
        Utility.enableDoneButtonForMultilineEditText(sentenceEditText);
    }

    @Override
    public void showSentence(String sentence) {
        sentenceEditText.setText(sentence);
        sentenceEditText.setSelection(sentence.length());
    }

    @Override
    public void showFlag(int flagId) {
        picasso
                .load(flagId)
                .into(flagImageView);
    }

    @Override
    public void closeAndReturnResult(String sentence) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SENTENCE_KEY, sentence);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showNeedInternetInfo() {
        Toast.makeText(this, R.string.missing_internet_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyDataChanged() {
        Utility.sendBroadcastUserSentencesChanged(this);
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