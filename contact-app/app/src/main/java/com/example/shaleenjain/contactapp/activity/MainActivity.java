package com.example.shaleenjain.contactapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.shaleenjain.contactapp.R;
import com.example.shaleenjain.contactapp.fragment.ContactsFragment;
import com.example.shaleenjain.contactapp.fragment.MessageLogFragment;
import com.example.shaleenjain.contactapp.utils.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);

        setSupportActionBar(mToolbar);
        setupViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ContactsFragment(), "Contacts");
        adapter.addFragment(new MessageLogFragment(), "Sent Messages");

        viewPager.setAdapter(adapter);
    }

}
