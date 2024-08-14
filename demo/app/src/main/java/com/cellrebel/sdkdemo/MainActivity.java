package com.cellrebel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cellrebel.sdk.CRMeasurementSDK;
import com.cellrebel.sdk.workers.TrackingManager;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

	private TextView logTextView;
	private ScrollView scrollView;
	private Button startButton;
	private EditText idleTimeEditText;
	private TextView measurementIdLabel;
	private TextView measurementIdValue;
	private Button copyButton;
	private TextView locationPermissionTextView;
	private Handler handler;
	private Runnable updateLogsRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		logTextView = findViewById(R.id.logTextView);
		scrollView = findViewById(R.id.scrollView);
		startButton = findViewById(R.id.startButton);
		idleTimeEditText = findViewById(R.id.idleTimeEditText);
		measurementIdLabel = findViewById(R.id.measurementIdLabel);
		measurementIdValue = findViewById(R.id.measurementIdValue);
		copyButton = findViewById(R.id.copyButton);
		locationPermissionTextView = findViewById(R.id.locationPermissionTextView);
		handler = new Handler(Looper.getMainLooper());

		// Check Location Permission
		checkLocationPermission();

		updateLogsRunnable = new Runnable() {
			@Override
			public void run() {
				String log = App.getLogMessages().toString();
				if (log.contains("VIDEO_TEST measurementSequenceId updated")) {
					measurementIdValue.setText(TrackingManager.getMeasurementSequenceId());
				}
				logTextView.setText(App.getLogMessages().toString());
				scrollView.post(new Runnable() {
					@Override
					public void run() {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
				handler.postDelayed(this, 100); // Update every second
			}
		};

		// Copy Button functionality
		copyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Measurement ID", measurementIdValue.getText().toString());
				clipboard.setPrimaryClip(clip);

				Toast.makeText(MainActivity.this, "Measurement ID copied to clipboard", Toast.LENGTH_SHORT).show();
			}
		});

		// Start the periodic update
		handler.post(updateLogsRunnable);

		Context context = this.getApplicationContext();

		// Set up the Start button
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the idle time from the EditText
				String idleTimeStr = idleTimeEditText.getText().toString();
				if (idleTimeStr.isEmpty()) {
					Toast.makeText(MainActivity.this, "Please enter the idle time", Toast.LENGTH_SHORT).show();
					return;
				}

				int idleTime = Integer.parseInt(idleTimeStr);

				// Call the SDK method with idle time
				TrackingManager.startTracking(context, idleTime);

				// Disable the button after pressing
				startButton.setEnabled(false);

				// Optionally, log the action
				Timber.d("Measurement started with idle time: %d seconds", idleTime);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(updateLogsRunnable);
	}

	private void checkLocationPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted, request it
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					LOCATION_PERMISSION_REQUEST_CODE);
		} else {
			// Permission is already granted
			locationPermissionTextView.setText("Location permission: Granted");
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions,
										   int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission was granted
				locationPermissionTextView.setText("Location permission: Granted");
				Timber.d("Location permission granted.");
			} else {
				// Permission was denied
				locationPermissionTextView.setText("Location permission: Denied");
				Timber.d("Location permission denied.");
			}
		}
	}
}
