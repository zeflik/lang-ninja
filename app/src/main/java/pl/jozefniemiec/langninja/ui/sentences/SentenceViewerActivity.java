package pl.jozefniemiec.langninja.ui.sentences;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCard;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceViewerActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        startLanguagesListForResult();
    }

    public void startLanguagesListForResult() {
        String languageCode = getIntent().getStringExtra(LANGUAGE_CODE_KEY);
        SentenceCard languagesFragment = SentenceCard.newInstance(languageCode);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sentenceCardFragmentContainer, languagesFragment)
                .commit();
    }
}
