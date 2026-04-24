package com.cellrebel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;

public class ConsentActivity extends AppCompatActivity {
    public static final String PREFS = "consent_prefs";
    public static final String CONSENT = "user_consent";

    public static final String CONSENT_ACCEPTED = "ACCEPTED";
    public static final String CONSENT_DECLINED = "DECLINED";
    public static final String CONSENT_NOT_SET = "NOT_SET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        Button btnAccept = findViewById(R.id.button_accept);
        Button btnDecline = findViewById(R.id.button_decline);

        btnAccept.setOnClickListener(v -> {
            saveConsent(CONSENT_ACCEPTED);
            Intent result = new Intent();
            result.putExtra("consent_result", true);
            setResult(RESULT_OK, result);
            finish();
        });

        btnDecline.setOnClickListener(v -> {
            saveConsent(CONSENT_DECLINED);
            Intent result = new Intent();
            result.putExtra("consent_result", false);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void saveConsent(String accepted) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS, MODE_PRIVATE);
        prefs.edit().putString(CONSENT, accepted).apply();
    }
}