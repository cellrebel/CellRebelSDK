package com.cellrebel.sdkdemo;

import android.arch.lifecycle.LifecycleObserver;
import android.support.multidex.MultiDexApplication;

import com.cellrebel.sdk.workers.TrackingManager;

public class App extends MultiDexApplication implements LifecycleObserver {

	@Override
	public void onCreate() {
		super.onCreate();

		TrackingManager.init(this);
	}
}
