package pl.jozefniemiec.langninja.activities.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.activities.main.fragments.home.view.HomeFragment;
import pl.jozefniemiec.langninja.di.ContextModule;
import pl.jozefniemiec.langninja.di.DaggerLangNinjaApplicationComponent;
import pl.jozefniemiec.langninja.di.FragmentManagerModule;
import pl.jozefniemiec.langninja.di.LangNinjaApplicationComponent;
import pl.jozefniemiec.langninja.model.Language;
import pl.jozefniemiec.langninja.repository.LanguageRepository;
import pl.jozefniemiec.langninja.repository.room.AppDatabase;

public class MainActivity extends AppCompatActivity {

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

        LangNinjaApplicationComponent component = DaggerLangNinjaApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .fragmentManagerModule(new FragmentManagerModule(getSupportFragmentManager()))
                .build();
        component.injectHomeActivity(this);

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
}
