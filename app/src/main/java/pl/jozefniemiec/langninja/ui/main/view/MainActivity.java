package pl.jozefniemiec.langninja.ui.main.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.main.presenter.MainPresenter;
import pl.jozefniemiec.langninja.ui.main.view.fragment.home.view.HomeFragment;

public class MainActivity extends DaggerAppCompatActivity implements MainView {

    @BindView(R.id.language_view_pager)
    ViewPager viewPager;

    @Inject
    SectionPageAdapter sectionPageAdapter;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sectionPageAdapter.addFragment(HomeFragment.newInstance(), "Home");
        viewPager.setAdapter(sectionPageAdapter);
        mainPresenter.loadMain();
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onExitCleanup();
        super.onDestroy();
    }

    @Override
    public void showFragments() {
    }
}
