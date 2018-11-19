package pl.jozefniemiec.langninja.ui.sentences;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.sentences.card.SentenceCard;
import pl.jozefniemiec.langninja.ui.sentences.reader.OnReaderFragmentInteractionListener;
import pl.jozefniemiec.langninja.ui.sentences.reader.ReaderFragment;

import static pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment.LANGUAGE_CODE_KEY;

public class SentenceViewerActivity extends DaggerAppCompatActivity
        implements OnReaderFragmentInteractionListener {

    SentenceCard sentenceCard;
    ReaderFragment readerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        attachFragments();
    }

    public void attachFragments() {
        String languageCode = getIntent().getStringExtra(LANGUAGE_CODE_KEY);
        sentenceCard = SentenceCard.newInstance(languageCode);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sentenceCardFragmentContainer, sentenceCard)
                .commit();

        readerFragment = ReaderFragment.newInstance(languageCode);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.readerFragmentContainer, readerFragment)
                .commit();
    }

    @Override
    public String getCurrentSentence() {
        return sentenceCard.getCurrentSentence();
    }
}
