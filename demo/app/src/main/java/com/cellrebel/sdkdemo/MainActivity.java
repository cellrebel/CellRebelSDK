package com.cellrebel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.cellrebel.sdk.CRMeasurementSDK;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CRMeasurementSDK.startMeasuring(this);
	}
}
