package pl.jozefniemiec.langninja.activities.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.HomeFragment;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.RoomLanguageRepository;
import pl.jozefniemiec.langninja.repository.room.AppDatabase;

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
