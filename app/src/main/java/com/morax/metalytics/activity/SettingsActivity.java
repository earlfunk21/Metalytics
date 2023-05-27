package com.morax.metalytics.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.User;

public class SettingsActivity extends AppCompatActivity {

    private UserDao userDao;
    private User user;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userDao = AppDatabase.getInstance(this).userDao();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        user = userDao.getUserById(userPrefs.getLong("user_id", 0));

        TextView tvUsername = findViewById(R.id.tv_username_settings);
        TextView tvFullName = findViewById(R.id.tv_full_name);

        tvUsername.setText(user.username);
        String fullName = user.firstname + " " + user.lastname;
        tvFullName.setText(fullName);
    }

    public void openEditProfile(View view) {
        Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }
}