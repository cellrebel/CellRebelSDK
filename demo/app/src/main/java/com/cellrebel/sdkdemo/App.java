package com.cellrebel.sdkdemo;

import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDexApplication;

import com.cellrebel.sdk.CRMeasurementSDK;

public class App extends MultiDexApplication implements LifecycleObserver {

	@Override
	public void onCreate() {
		super.onCreate();

		CRMeasurementSDK.init(this, "d7mrw1n1ig", "DEVICE_ID");
	}
}
