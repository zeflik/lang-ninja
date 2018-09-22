package pl.jozefniemiec.langninja.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.room.AppDatabase;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragment;

public class MainActivity extends DaggerAppCompatActivity implements MainView {

    @BindView(R.id.language_view_pager)
    ViewPager mViewPager;

    @Inject
    SectionPageAdapter mSectionsPageAdapter;

    @Inject
    LanguageRepository languageRepository;

    @Inject
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initializeDatabase();
        mSectionsPageAdapter.addFragment(homeFragment, "Home");
        mViewPager.setAdapter(mSectionsPageAdapter);
    }

    private void initializeDatabase() {

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

    @Override
    public void onMainLoaded() {

    }
}
