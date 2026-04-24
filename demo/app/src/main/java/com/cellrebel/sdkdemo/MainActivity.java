package com.cellrebel.sdkdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.cellrebel.sdk.CRMeasurementSDK;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "CellRebelSDKDemo";

	private final ActivityResultLauncher<String> readPhoneStatePermissionLauncher =
			registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					Log.d(TAG, "Read phone state permission granted.");
				} else {
					Log.d(TAG, "Read phone state permission denied.");
				}
			});

	private final ActivityResultLauncher<String> locationPermissionLauncher =
			registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					Log.d(TAG, "Location permission granted.");
					CRMeasurementSDK.startMeasuring(this);
				} else {
					Log.d(TAG, "Location permission denied.");
				}
			});

	private final ActivityResultLauncher<Intent> consentLauncher =
			registerForActivityResult(
					new ActivityResultContracts.StartActivityForResult(),
					result -> {
						if (result.getResultCode() == RESULT_OK) {
							Intent data = result.getData();

							if (data != null) {
								boolean accepted = data.getBooleanExtra("consent_result", false);

								if (accepted) {
									Log.d(TAG, "User accepted consent");
									proceedAfterConsent();
								} else {
									Log.d(TAG, "User declined consent");
								}
							}
						}
					}
			);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, ConsentActivity.class);

		String status = hasUserConsented();
		if (savedInstanceState == null && status.equals(ConsentActivity.CONSENT_NOT_SET)) {
			consentLauncher.launch(intent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		String status = hasUserConsented();
		if (status.equals(ConsentActivity.CONSENT_ACCEPTED)) {
			boolean alreadyAsked = getPreferences(MODE_PRIVATE).getBoolean("asked_phone_state", false);

			// Optionally ask for READ_PHONE_STATE permission
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
					&& !alreadyAsked) {
				requestPhoneState();
				getPreferences(MODE_PRIVATE).edit().putBoolean("asked_phone_state", true).apply();
			}

			proceedAfterConsent();
		}
	}

	private String hasUserConsented() {
		SharedPreferences prefs = this.getSharedPreferences(ConsentActivity.PREFS, MODE_PRIVATE);

		return prefs.getString(ConsentActivity.CONSENT, ConsentActivity.CONSENT_NOT_SET);
	}

	/*
	* IMPORTANT:
	* Measurements should only start after user consent + location permission.
	* This ensures compliance with privacy requirements.
	*/
	private void proceedAfterConsent() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Location permission already granted.");
			CRMeasurementSDK.startMeasuring(this);
		} else {
			locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
		}
	}

	private void requestPhoneState() {
		new AlertDialog.Builder(this)
				.setTitle("Optional: Network Quality")
				.setMessage("To provide more accurate signal strength data, we can use your phone's status. Android calls this 'Phone Calls' permission, but we only use it to read signal info.")
				.setPositiveButton("I'm Okay With That", (dialog, which) -> {
					readPhoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
				})
				.setNegativeButton("No Thanks", (dialog, which) -> {
					Log.d(TAG, "User opted out of optional signal measurement.");
				})
				.show();
	}
}
