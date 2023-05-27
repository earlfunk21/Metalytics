package com.morax.metalytics.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.User;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    private UserDao userDao;
    private User user;
    private SharedPreferences userPrefs;
    private CancellationSignal cancellationSignal = null;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        userDao = AppDatabase.getInstance(this).userDao();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        user = userDao.getUserById(userPrefs.getLong("user_id", 0));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void updatePassword(View view) {

        TextInputEditText etCurrentPassword = findViewById(R.id.et_current_password);
        TextInputEditText etNewPassword = findViewById(R.id.et_password_change);
        TextInputEditText etNewPassword1 = findViewById(R.id.et_password1_change);

        String currentPassword = Objects.requireNonNull(etCurrentPassword.getText()).toString();
        String newPassword = Objects.requireNonNull(etNewPassword.getText()).toString();
        String newPassword1 = Objects.requireNonNull(etNewPassword1.getText()).toString();

        if (currentPassword.equals("")
                || newPassword.equals("")
                || newPassword1.equals("")) {
            Toast.makeText(this, "Fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!user.password.equals(currentPassword)) {
            Toast.makeText(this, "Invalid Current Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(newPassword1)) {
            Toast.makeText(this, "Password not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(
                    int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser("Authentication Error : " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                user.password = newPassword;
                userDao.update(user);
                Toast.makeText(ChangePassword.this, "Successfully Password Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassword.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt
                .Builder(getApplicationContext())
                .setTitle("Confirmation of user")
                .setSubtitle("need to confirm the user")
                .setDescription("Uses FP")
                .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void
                    onClick(DialogInterface dialogInterface, int i) {
                        notifyUser("Authentication Cancelled");
                    }
                }).build();

        // start the authenticationCallback in
        // mainExecutor
        biometricPrompt.authenticate(
                getCancellationSignal(),
                getMainExecutor(),
                authenticationCallback);

        checkBiometricSupport();

    }

    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(
                new CancellationSignal.OnCancelListener() {
                    @Override
                    public void onCancel() {
                        notifyUser("Authentication was Cancelled by the user");
                    }
                });
        return cancellationSignal;
    }

    private void checkBiometricSupport() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isDeviceSecure()) {
            notifyUser("Fingerprint authentication has not been enabled in settings");
            openSecuritySettings();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled");
            openAppSettings();
            return;
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
        }
    }

    // this is a toast method which is responsible for
    // showing toast it takes a string as parameter
    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openSecuritySettings() {
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}