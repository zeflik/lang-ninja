package com.example.joseph.langninja;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.joseph.langninja.dao.AppDatabase;
import com.example.joseph.langninja.dao.LanguageDao;
import com.example.joseph.langninja.fragments.home.HomeFragment;
import com.example.joseph.langninja.model.Language;

public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LanguageDao languageDao = AppDatabase.getInstance(getApplication()).languageDao();
        languageDao.insertAll(
                new Language("pl_PL", "Polski"),
                new Language("en_GB", "English"),
                new Language("de", "German")
        );
        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        mSectionsPageAdapter.addFragment(homeFragment, "Home");
        mViewPager = findViewById(R.id.language_view_pager);
        mViewPager.setAdapter(mSectionsPageAdapter);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
