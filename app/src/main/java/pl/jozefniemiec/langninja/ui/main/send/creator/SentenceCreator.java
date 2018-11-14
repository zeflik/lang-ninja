package pl.jozefniemiec.langninja.ui.main.send.creator;

import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;

public class SentenceCreator extends DaggerAppCompatActivity implements SentenceCreatorContract.View {

    @BindView(R.id.sentenceLanguageInput)
    EditText sentenceLanguageInput;

    @BindView(R.id.sentenceTextInput)
    EditText sentenceTextInput;

    @BindView(R.id.sentenceCreateButton)
    Button sentenceCreateButton;

    @Inject
    SentenceCreatorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sentence);
        ButterKnife.bind(this);
        sentenceTextInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sentenceTextInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
        sentenceCreateButton.setOnClickListener(v -> {
            String langCode = sentenceTextInput.getText().toString();
            String sentence = sentenceLanguageInput.getText().toString();
            presenter.createButtonClicked(langCode, sentence);
        });
        presenter.onViewCreated();
    }

    @Override
    public void close() {
        finish();
    }
}
