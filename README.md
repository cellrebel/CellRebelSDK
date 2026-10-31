# CellRebel Android SDK

## Notice for partners using CellRebelSDK 1.9.23 and newer

CellRebelSDK updated `play-services-location` library to version 21.0.1 which is not backward compatible with older versions. If your app is still using older `play-services-location` please consider updating it to the latest version or switching to the support version of CellRebelSDK (https://github.com/cellrebel/CellRebelSDK/tree/support) which still uses the older dependencies. 

## Download

Add CellRebel Maven repository in project `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven {
            url 'https://raw.githubusercontent.com/cellrebel/CellRebelSDK/beta/releases'
        }
        ...
}
```

Then, add the library dependency to module `build.gradle`:
```gradle
dependencies {
    ...
    implementation 'com.cellrebel.android:cellrebel-sdk:1.12.2'
}
```

## Usage

Initialize SDK using your unique CLIENT_KEY string on application create:
```java
import com.cellrebel.sdk.workers.TrackingManager;

public class App extends MultiDexApplication implements LifecycleObserver {

	@Override
	public void onCreate() {
		super.onCreate();

        CRMeasurementSDK.init(this, "CLIENT_KEY");
	}
}
```

Use `startMeasuring` to start measurement. On the first launch it's best to call this method after user response on location permission dialog. During the next sessions this method should be called on main activity onCreate lifecycle callback:
```java
CRMeasurementSDK.startMeasuring(this);
```
No measurements will be done until `startMeasuring` is called again (you as a developer have full control on when the sampling is done).

In some (rare) cases, if very high load tasks need to be performed, `stopMeasuring` can be used to abort an ongoing measurement sequence:
```java
CRMeasurementSDK.stopMeasuring(this);
```
There is no need of calling `stopMeasuring` when the application is closed, as it stops all SDK related activities including sending reports. 

## GPS truncation

Since 1.12.0, you can reduce the precision of the location the SDK collects. While truncation is enabled, latitude and longitude are truncated toward zero to 2 decimal places (about 1 km) before they are stored or sent, and each measurement reports `isGpsTruncated = true`:
```java
CRMeasurementSDK.setGpsTruncationEnabled(true);
```
Truncation is disabled by default. The setting is kept in memory only, so it resets to disabled every time the app process starts, including when the system starts the process to run background measurements. Call it in `Application.onCreate`, before `CRMeasurementSDK.init`:
```java
@Override
public void onCreate() {
    super.onCreate();

    CRMeasurementSDK.setGpsTruncationEnabled(true);
    CRMeasurementSDK.init(this, "CLIENT_KEY");
}
```
The change takes effect immediately, including for a measurement that is already running. Measurements captured before the change keep the precision they were captured with. Call `CRMeasurementSDK.setGpsTruncationEnabled(false)` to go back to full precision.

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
