package com.cellrebel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.cellrebel.sdk.workers.TrackingManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TrackingManager.startTracking(this);
	}
}
