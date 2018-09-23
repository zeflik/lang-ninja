package pl.jozefniemiec.langninja.ui.language.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;
import pl.jozefniemiec.langninja.ui.language.view.adapter.SentencesPageAdapter;

import static pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment.LANGUAGE_CODE;

public class SentenceCard extends DaggerAppCompatActivity
        implements SentenceCardView, ViewPager.OnPageChangeListener {

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardPresenter presenter;

    @BindView(R.id.language_card_view_pager)
    ViewPager viewPager;

    @BindView(R.id.languagePageCount)
    TextView numberingTv;

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
        presenter.onPageChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
