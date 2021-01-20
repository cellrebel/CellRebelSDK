# CellRebel Android SDK

## Download

Add CellRebel Maven repository in project `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven {
            url 'https://raw.githubusercontent.com/cellrebel/CellRebelSDK/rc/releases'
        }
        ...
}
```

Then, add the library dependency to module `build.gradle`:
```gradle
dependencies {
    ...
    implementation 'com.cellrebel.android:cellrebel-sdk:1.7.1'
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

Initialize SDK using your unique CLIENT_KEY string on application onCreate event:
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

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/rc/demo
