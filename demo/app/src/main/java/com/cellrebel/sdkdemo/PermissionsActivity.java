package com.cellrebel.sdkdemo;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {
    public static final String PERMISSIONS = "user_permissions";
    private final ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                Boolean locationGranted = isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean phoneStateGranted = isGranted.get(Manifest.permission.READ_PHONE_STATE);

                if (locationGranted != null && phoneStateGranted != null) {
                    setAskedPermission();
                    setResult(RESULT_OK);
                }

                finish();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions);

        Button btnAccept = findViewById(R.id.permissions_accept);
        Button btnDecline = findViewById(R.id.permissions_decline);

        btnAccept.setOnClickListener(v -> {
            permissionsLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
            });
        });

        btnDecline.setOnClickListener(v -> {
            setAskedPermission();
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void setAskedPermission() {
        this.getSharedPreferences(PermissionsActivity.PERMISSIONS, MODE_PRIVATE).edit().putBoolean("asked_permissions", true).apply();
    }
}