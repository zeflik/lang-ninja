package pl.jozefniemiec.langninja.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.main.languages.LanguagesFragment;
import pl.jozefniemiec.langninja.ui.main.send.SendFragment;

public class MainActivity extends DaggerAppCompatActivity implements MainView {

    @BindView(R.id.language_view_pager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Inject
    SectionPageAdapter sectionPageAdapter;

    @Inject
    MainPresenter mainPresenter;


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
                SendFragment.newInstance(),
                getString(R.string.tabs_send_tongue_twister)
        );
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(sectionPageAdapter);
    }
}
