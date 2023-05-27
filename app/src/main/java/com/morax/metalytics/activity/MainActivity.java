package com.morax.metalytics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.morax.metalytics.R;
import com.morax.metalytics.adapter.HomeLayoutAdapter;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;

public class MainActivity extends AppCompatActivity {
    private UserDao userDao;
    private SharedPreferences userPrefs;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager2.setUserInputEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeLayoutAdapter homeLayoutAdapter = new HomeLayoutAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(homeLayoutAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void openSidebar(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeSidebar() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeSidebar();
    }

    public void logoutUser(View view) {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public void openSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}