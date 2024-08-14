package com.cellrebel.sdkdemo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDexApplication;

import com.cellrebel.sdk.CRMeasurementSDK;

import timber.log.Timber;

public class App extends MultiDexApplication implements LifecycleObserver {

	private static StringBuilder logMessages = new StringBuilder();

	@Override
	public void onCreate() {
		super.onCreate();

		CRMeasurementSDK.init(this, "ynsf0bi7ag");

		Timber.plant(new Timber.DebugTree() {
			@Override
			protected void log(int priority, String tag, @NonNull String message, Throwable t) {
				super.log(priority, tag, message, t);
				if (message.startsWith("VIDEO_TEST")) {
					logMessages.append(message).append("\n");
				}
			}
		});
	}

	public static StringBuilder getLogMessages() {
		return logMessages;
	}
}
