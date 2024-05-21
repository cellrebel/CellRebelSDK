# CellRebel Android SDK

## Notice for existing users updating to CellRebelSDK 1.7.0 and newer

CellRebelSDK is no longer requires InitProvider to be added to the application `manifest`. Please remove SdkInitProvider from your application Manifest file after update.

## Download

Add CellRebel Maven repository in project `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven {
            url 'https://raw.githubusercontent.com/cellrebel/CellRebelSDK/1.8.0-rc/releases'
        }
        ...
}
```

Then, add the library dependency to module `build.gradle`:
```gradle
dependencies {
    ...
    implementation 'com.cellrebel.android:cellrebel-sdk:1.9.38-rc1'
}
```

CellRebel SDK requires Java 8, add target and source compatibility to android compile options in module `build.gradle`:
```gradle
android {
    ...
    compileOptions {
        targetCompatibility 11
        sourceCompatibility 11
    }
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

		TrackingManager.init(this, "CLIENT_KEY");
	}
}
```

Use `startTracking` to start measurement. On the first launch it's best to call this method after user response on location permission dialog. During the next sessions this method should be called on main activity onCreate lifecycle callback:
```java
TrackingManager.startTracking(this);
```
In some (rare) cases, if very high load tasks need to be performed, `stopTracking` can be used to abort an ongoing measurement sequence:
```java
TrackingManager.stopTracking();
```

Call `getVersion` to retrieve current version of CellRebelSDK:
```java
String cellRebelSDKVersion = TrackingManager.getVersion();
```

Use `clearUserData` if you need to request the removal of user data collected (based on GDPR 'right to be forgotten'):
```java
TrackingManager.clearUserData(context, new TrackingManager.OnCompleteListener() {
    @Override
    public void onCompleted(boolean success) {
        // Handle result
    }
});
```

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
