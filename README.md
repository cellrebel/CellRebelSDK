# CellRebel Android SDK

## Download

Add CellRebel Maven repository in project `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven {
            url 'https://raw.githubusercontent.com/cellrebel/CellRebelSDK/hotfix/releases'
        }
        ...
}
```

Then, add the library dependency to module `build.gradle`:
```gradle
dependencies {
    ...
    implementation 'com.cellrebel.android:cellrebel-sdk:1.9.52'
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

### Note about passive measurement collection
As of SDK version 1.9.40, a configuration refresh from CellRebel's web service is performed whenever the SDK internally references settings. Therefore, any attempted measurement, active or passive, will always first fetch a fresh configuration. This allows disabling of any or all measurement types through the CellRebel remote service, regardless of whether `startMeasuring` or `stopMeasuring` has been called.

This addresses a limitation in previous versions' ability to disable measurements, where configuration refresh was only performed by the SDK after `startMeasuring` was called, which led to passive measurements triggered by `init` to still be collected, even when `startMeasuring` was not called or even when `stopMeasuring` was called, since a cached configuration was instructing the SDK to continue with passive measurements.

### Utility interfaces
Call `getVersion` to retrieve current version of CellRebelSDK:
```java
String cellRebelSDKVersion = CRMeasurementSDK.getVersion();
```

Use `clearUserData` if you need to request the removal of user data collected (based on GDPR "right to be forgotten"):
```java
CRMeasurementSDK.clearUserData(context, new CRMeasurementSDK.OnCompleteListener() {
    @Override
    public void onCompleted(boolean success) {
        // Handle result
    }
});
```

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
