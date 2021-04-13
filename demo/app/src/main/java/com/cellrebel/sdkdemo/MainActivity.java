package com.cellrebel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cellrebel.sdk.workers.TrackingManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView textView = findViewById(R.id.tvVersion);
		ProgressBar progressBar = findViewById(R.id.progress);
		Button button = findViewById(R.id.btClearUserData);
		progressBar.setVisibility(View.INVISIBLE);
		TrackingManager.startTracking(this);
		textView.setText(getString(R.string.sdk_version) + TrackingManager.getVersion());
		button.setOnClickListener(v -> {
			progressBar.setVisibility(View.VISIBLE);
			button.setEnabled(false);
			TrackingManager.clearUserData(this, success -> {
				progressBar.setVisibility(View.INVISIBLE);
				String text = success ? "Success" : "Error";
				Toast toast = Toast.makeText(getApplicationContext(),
						text, Toast.LENGTH_SHORT);
				toast.show();
			});
		});
	}
}
