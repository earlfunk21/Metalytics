package com.morax.metalytics.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.User;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private UserDao userDao;
    private User user;
    private SharedPreferences userPrefs;

    private TextInputEditText etUsername;
    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userDao = AppDatabase.getInstance(this).userDao();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        user = userDao.getUserById(userPrefs.getLong("user_id", 0));

        etUsername = findViewById(R.id.et_username_edit);
        etFirstName = findViewById(R.id.et_firstname_edit);
        etLastName = findViewById(R.id.et_lastname_edit);

        etUsername.setText(user.username);
        etFirstName.setText(user.firstname);
        etLastName.setText(user.lastname);
    }

    public void updateUser(View view) {
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String firstname = Objects.requireNonNull(etFirstName.getText()).toString();
        String lastname = Objects.requireNonNull(etLastName.getText()).toString();

        if (username.equals("")
                || firstname.equals("")
                || lastname.equals("")) {
            Toast.makeText(this, "Fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        user.username = username;
        user.firstname = firstname;
        user.lastname = lastname;
        userDao.update(user);
        Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}