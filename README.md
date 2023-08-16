# CellRebel Android SDK in JAR file


## Download

Download the library [here](https://github.com/cellrebel/CellRebelSDK/blob/jar/cellrebel-sdk.jar?raw=true)

## Install

Add the library and dependency to module `build.gradle`:
```gradle
dependencies {
    ...
     implementation files('libs/cellrebel-sdk.jar')
     
     // add other needed libraries if you don't alrady use it
     
    implementation 'com.google.android.gms:play-services-location:20.0.0'
    implementation "com.squareup.retrofit2:retrofit:2.6.4"
    implementation "androidx.lifecycle:lifecycle-common:2.5.1"
    implementation "androidx.lifecycle:lifecycle-process:2.5.1"
    implementation "androidx.lifecycle:lifecycle-runtime:2.5.1"
    
    api "com.squareup.retrofit2:converter-gson:2.6.4"
    api 'androidx.work:work-runtime:2.7.1'
  
}
```

Add permissions in AndroidManifest.xml file
```
<manifest ... >
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   
    <application>
    ...
     <receiver
            android:name="com.cellrebel.sdk.utils.PhoneStateReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.PHONE_STATE" />
            </intent-filter>
        </receiver>
    </application>
</manifest>
```

## Usage

Initialize SDK using your unique CLIENT_KEY string on application create:
```java
import com.cellrebel.sdk.workers.TrackingManager;

public class App extends MultiDexApplication implements LifecycleObserver {

	@Override
	public void onCreate() {
		super.onCreate();

		TrackingManager.init(this, "CLIENT_KEY");
	}
}
```

Use `startTracking` to start measurement. On the first launch it's best to call this method after user response on location permission dialog. During the next sessions this method should be called on main activity onCreate lifecycle callback:
```java
TrackingManager.startTracking(this);
```
No measurements will be done until `startTracking` is called again (you as a developer have full control on when the sampling is done).

In some (rare) cases, if very high load tasks need to be performed, `stopTracking` can be used to abort an ongoing measurement sequence:
```java
TrackingManager.stopTracking();
```
There is no need of calling `stopTracking` when the application is closed, as it stops all SDK related activities including sending reports. 

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/jar/demo
