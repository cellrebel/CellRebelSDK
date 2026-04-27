package com.cellrebel.sdkdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.cellrebel.sdk.CRMeasurementSDK;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "CellRebelSDKDemo";

	private final ActivityResultLauncher<Intent> permissionsLauncher =
			registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
				if (result.getResultCode() == RESULT_OK) {
					Log.d(TAG, "All required permissions complete.");
				} else if (result.getResultCode() == RESULT_CANCELED) {
					Log.d(TAG, "User explicitly declined permission prompt.");
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
		boolean alreadyRequested = this.getSharedPreferences(PermissionsActivity.PERMISSIONS, MODE_PRIVATE).getBoolean("asked_permissions", false);

		/*
		 * IMPORTANT:
		 * Measurements should only start after user consent is accepted and runtime permissions have been requested.
		 * This ensures compliance with privacy requirements.
		 */
		if (status.equals(ConsentActivity.CONSENT_ACCEPTED)) {
			if (alreadyRequested) {
				CRMeasurementSDK.startMeasuring(this);
			} else {
				Intent intent = new Intent(this, PermissionsActivity.class);
				permissionsLauncher.launch(intent);
			}
		}
	}

	private String hasUserConsented() {
		SharedPreferences prefs = this.getSharedPreferences(ConsentActivity.PREFS, MODE_PRIVATE);
		return prefs.getString(ConsentActivity.CONSENT, ConsentActivity.CONSENT_NOT_SET);
	}
}
