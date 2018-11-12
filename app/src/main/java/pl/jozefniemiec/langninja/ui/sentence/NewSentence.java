package pl.jozefniemiec.langninja.ui.sentence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public class NewSentence extends AppCompatActivity {

    @BindView(R.id.sentenceLanguageInput)
    EditText sentenceLanguageInput;

    @BindView(R.id.sentenceTextInput)
    EditText sentenceTextInput;

    @BindView(R.id.sentenceCreateButton)
    Button sentenceCreateButton;

    private DatabaseReference dbSentencesRef = FirebaseDatabase.getInstance().getReference("sentence");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sentence);
        ButterKnife.bind(this);
        sentenceTextInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sentenceTextInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
        sentenceCreateButton.setOnClickListener(v -> createSentence());
    }

    private void createSentence() {
        Sentence sentence = new Sentence(
                sentenceTextInput.getText().toString(),
                sentenceLanguageInput.getText().toString()
        );
        dbSentencesRef.push().setValue(sentence);
        finish();
    }
}
