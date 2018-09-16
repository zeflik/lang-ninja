package com.example.joseph.langninja;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.joseph.langninja.fragments.home.HomeFragment;
import com.example.joseph.langninja.model.Language;
import com.example.joseph.langninja.repository.LanguageRepository;
import com.example.joseph.langninja.repository.RoomLanguageRepository;
import com.example.joseph.langninja.repository.room.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDatabase();
        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        mSectionsPageAdapter.addFragment(homeFragment, "Home");
        mViewPager = findViewById(R.id.language_view_pager);
        mViewPager.setAdapter(mSectionsPageAdapter);
    }

    private void initializeDatabase() {
        LanguageRepository languageRepository = new RoomLanguageRepository(getApplicationContext());
        languageRepository.insertAll(
                new Language("pl_PL", "Polski"),
                new Language("en_GB", "English"),
                new Language("de", "German")
        );
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
