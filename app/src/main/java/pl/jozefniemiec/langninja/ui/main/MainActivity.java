package pl.jozefniemiec.langninja.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.main.community.CommunityFragment;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment;
import pl.jozefniemiec.langninja.ui.profile.UserProfileActivity;

public class MainActivity extends DaggerAppCompatActivity implements MainContract.View {

    @BindView(R.id.language_view_pager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Inject
    SectionPageAdapter sectionPageAdapter;

    @Inject
    MainContract.Presenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter.loadMain();
    }

    @Override
    public void showFragments() {
        sectionPageAdapter.addFragment(
                LanguagesFragment.newInstance(),
                getString(R.string.tabs_tongue_twisters)
        );
        sectionPageAdapter.addFragment(
                CommunityFragment.newInstance(),
                getString(R.string.tabs_send_tongue_twister)
        );
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(sectionPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
