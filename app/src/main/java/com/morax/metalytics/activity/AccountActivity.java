package com.morax.metalytics.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.User;
import com.morax.metalytics.fragment.LoginFragment;
import com.morax.metalytics.fragment.RegisterFragment;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    private UserDao userDao;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();
        BottomNavigationView bnvAccount = findViewById(R.id.bnv_account);
        loadFragment(new LoginFragment());
        bnvAccount.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_register) {
                    loadFragment(new RegisterFragment());
                } else {
                    loadFragment(new LoginFragment());
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        long user_id = userPrefs.getLong("user_id", 0);
        User user = userDao.getUserById(user_id);
        if (user != null && user_id != 0) {
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_account, fragment)
                .commit();
    }

    public void registerUser(View view) {
        TextInputEditText etUsername = findViewById(R.id.et_username_register);
        TextInputEditText etPassword = findViewById(R.id.et_password_register);
        TextInputEditText etPassword1 = findViewById(R.id.et_password1);
        TextInputEditText etFirstName = findViewById(R.id.et_firstname);
        TextInputEditText etLastName = findViewById(R.id.et_lastname);
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();
        String confirm_password = Objects.requireNonNull(etPassword1.getText()).toString();
        String firstname = Objects.requireNonNull(etFirstName.getText()).toString();
        String lastname = Objects.requireNonNull(etLastName.getText()).toString();


        if (password.equals("")
                || confirm_password.equals("")
                || username.equals("")
                || firstname.equals("")
                || lastname.equals("")) {
            Toast.makeText(this, "Fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Password must match!", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            Toast.makeText(this, "Username is already exists!", Toast.LENGTH_SHORT).show();
            return;
        }
        user = new User(
                username, password, firstname, lastname
        );
        userDao.insert(user);
        Toast.makeText(this, "Successfully Created!", Toast.LENGTH_SHORT).show();
        loadFragment(new LoginFragment());

    }


    public void loginUser(View view) {
        TextInputEditText etUsername = findViewById(R.id.et_username);
        TextInputEditText etPassword = findViewById(R.id.et_password);
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();

        User user = userDao.checkUser(username, password);
        if (user == null) {
            Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putLong("user_id", user.id);
        editor.apply();
    }
}