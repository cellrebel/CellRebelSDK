# CellRebel Android SDK

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
    implementation 'com.cellrebel.android:cellrebel-sdk:1.5.6'
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
CellRebel SDK requires InitProvider to be added to the application `manifest`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.cellrebelsdkdemoapp">

    <application
       ...
        <provider
            android:name="com.cellrebel.sdk.SdkInitProvider"
            android:authorities="${applicationId}.SdkInitProvider"
            android:enabled="true"
            android:exported="false" />
    </application>
</manifest>
```

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

Use `startTracking` to start:
```java
TrackingManager.startTracking(this);
```

Use `stopTracking` if you need to manually stop measurement (for example when performing high load tasks):
```java
TrackingManager.stopTracking();
```

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
