package com.morax.metalytics.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.fragment.HomeFragment;
import com.morax.metalytics.fragment.PostFragment;

public class MainActivity extends AppCompatActivity {
    private UserDao userDao;
    private SharedPreferences userPrefs;
    private DrawerLayout drawerLayout;
    private SharedPreferences nightModePrefs;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();
        nightModePrefs = getSharedPreferences("nightModePrefs", MODE_PRIVATE);
        nightMode = nightModePrefs.getBoolean("mode", false);
        if (nightMode) {
            SwitchCompat switchCompat = findViewById(R.id.switch_mode);
            switchCompat.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        changeViewContent(new HomeFragment());
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

    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changeMode() {
        SharedPreferences.Editor editor;
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = nightModePrefs.edit();
            editor.putBoolean("mode", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = nightModePrefs.edit();
            editor.putBoolean("mode", true);
        }
        Toast.makeText(this, "Changing Theme..", Toast.LENGTH_SHORT).show();
        editor.apply();
    }

    public void switchMode(View view) {
        changeMode();
    }

    public void changePostView(View view) {
        changeViewContent(new PostFragment());
    }

    public void changeViewContent(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_content, fragment)
                .commit();
        closeSidebar();
    }

    public void changeHomeView(View view) {
        changeViewContent(new HomeFragment());
    }

    public void changeQuizView(View view) {
        changeViewContent(new QuizFragment());
    }
}