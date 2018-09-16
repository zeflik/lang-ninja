package pl.jozefniemiec.langninja.activities.language.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.language.view.pages.view.LanguagePageAdapter;

public class LanguageCard extends AppCompatActivity {

    private ViewPager mViewPager;
    private LanguagePageAdapter languagePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_card);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        languagePageAdapter = new LanguagePageAdapter(getBaseContext());
        mViewPager = findViewById(R.id.language_card_view_pager);
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
