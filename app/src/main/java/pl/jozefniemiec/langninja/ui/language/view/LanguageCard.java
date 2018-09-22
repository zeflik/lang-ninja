package pl.jozefniemiec.langninja.ui.language.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenter;
import pl.jozefniemiec.langninja.ui.language.presenter.LanguageCardPresenterImpl;
import pl.jozefniemiec.langninja.ui.language.view.adapter.view.LanguagePageAdapter;

import static pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragment.LANGUAGE_CODE;

public class LanguageCard extends AppCompatActivity implements LanguageCardView {

    private LanguagePageAdapter languagePageAdapter;
    private LanguageCardPresenter presenter;

    @BindView(R.id.language_card_view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_card);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(getIntent().getStringExtra(LANGUAGE_CODE));

        presenter = new LanguageCardPresenterImpl(this);
        presenter.loadData();
    }

    @Override
    public void showData() {
        languagePageAdapter = new LanguagePageAdapter(getBaseContext());
        mViewPager.setAdapter(languagePageAdapter);
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
}
