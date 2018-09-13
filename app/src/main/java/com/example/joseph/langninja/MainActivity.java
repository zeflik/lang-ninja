package com.example.joseph.langninja;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.joseph.langninja.main.HomeFragment;
import com.example.joseph.langninja.main.MainPresenter;
import com.example.joseph.langninja.main.SectionPageAdapter;

public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        mSectionsPageAdapter.addFragment(homeFragment, "Home");
        presenter = new MainPresenter(homeFragment);
        mViewPager = findViewById(R.id.language_view_pager);
        mViewPager.setAdapter(mSectionsPageAdapter);
    }
}
