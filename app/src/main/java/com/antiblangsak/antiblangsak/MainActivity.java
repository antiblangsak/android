package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private Button logoutButton;
    SharedPrefManager sharedPrefManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        logoutButton = (Button) findViewById(R.ID.btnLogout);

        sharedPrefManager = new SharedPrefManager(this);

        int id = sharedPrefManager.getId();
        String token = sharedPrefManager.getToken();
        Log.w("USER_ID", "" + id);
        Log.w("API_TOKEN", "" + token);

//        logoutButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
//
//
//                startActivity(new Intent(MainActivity.this, LoginActivity.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                finish();
//            }
//
//
//
//        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(R.color.color_main_light_gray, R.color.color_main_dark_gray);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.w("TAB", "tab " + tab.getPosition() + " selected");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.w("TAB", "tab " + tab.getPosition() + " unselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
