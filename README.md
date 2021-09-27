# CellRebel Android SDK

## Notice for existing users updating to CellRebelSDK 1.7.0 and newer

CellRebelSDK is no longer requires InitProvider to be added to the application `manifest`. Please remove SdkInitProvider from your application Manifest file after update.

## Download

Add CellRebel Maven repository in project `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven {
            url 'https://raw.githubusercontent.com/cellrebel/CellRebelSDK/master/releases'
        }
        ...
}
```

Then, add the library dependency to module `build.gradle`:
```gradle
dependencies {
    ...
    implementation 'com.cellrebel.android:cellrebel-sdk:1.8.6'
}
```

CellRebel SDK requires Java 8, add target and source compatibility to android compile options in module `build.gradle`:
```gradle
android {
    ...
    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
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
No measurements will be done until `startTracking` is called again (you as a developer have full control on when the sampling is done).

In some (rare) cases, if very high load tasks need to be performed, `stopTracking` can be used to abort an ongoing measurement sequence:
```java
TrackingManager.stopTracking();
```

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
