package pl.jozefniemiec.langninja.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.main.fragment.home.view.HomeFragment;

public class MainActivity extends DaggerAppCompatActivity implements MainView {

    @BindView(R.id.language_view_pager)
    ViewPager mViewPager;

    @Inject
    SectionPageAdapter mSectionsPageAdapter;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSectionsPageAdapter.addFragment(HomeFragment.newInstance(), "Home");
        mViewPager.setAdapter(mSectionsPageAdapter);
        mainPresenter.loadMain();
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onExitCleanup();
        super.onDestroy();
    }

    @Override
    public void showFragments() {
        Toast.makeText(this, "Main Activity loaded", Toast.LENGTH_SHORT).show();
    }
}
