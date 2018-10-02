package pl.jozefniemiec.langninja.ui.language.view;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;

import static pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment.LANGUAGE_CODE;

public class SentenceCard extends DaggerAppCompatActivity
        implements SentenceCardView, ViewPager.OnPageChangeListener, TextToSpeech.OnInitListener {

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardPresenter presenter;

    @Inject
    TextToSpeech textToSpeech;

    @BindView(R.id.language_card_view_pager)
    ViewPager viewPager;

    @BindView(R.id.languagePageCount)
    TextView numberingTv;

    @BindView(R.id.language_card_play_button)
    ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_card);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(getIntent().getStringExtra(LANGUAGE_CODE));

        presenter.loadData(getIntent().getStringExtra(LANGUAGE_CODE));
    }

    @Override
    public void showData() {
        viewPager.setAdapter(languagePageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void showNumbering(String numbering) {
        numberingTv.setText(numbering);
    }

    @Override
    public int speak(String text) {
        return textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void stopSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    public void setReaderLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
            textToSpeech.setLanguage(locale);
        } else {
            presenter.readerLanguageNotSupported(languageCode);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPlayButton() {
        playButton.setOnClickListener(x -> presenter.playButtonClicked());
        playButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlayButton() {
        playButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.pageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onInit(int status) {
        presenter.readerInitialized(status == TextToSpeech.SUCCESS);
    }

    @Override
    protected void onPause() {
        stopSpeaking();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
