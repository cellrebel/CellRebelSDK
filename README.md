# CellRebel Android SDK

## Additional notes about 1.9.65-rc1 release

1.9.65-rc1 version of SDK has some critical changes in dependencies that might require your attention:
`androidx.core:core-ktx` and `androidx.core:core` are now updated to version `1.17.0`. If your app doesn't set `compileSDK` to API level 36, the app will not compile. Please make sure to set `compileSdk` to API 36.
We’re now using Exoplayer `2.19.1` so if your app (or any of your dependencies) also use Exoplayer, please make sure to update it to the version `2.19.1`. If update is not possible in a timely manner, make sure to force your current dependency version to make sure that nothing brakes. You can do that in your build.gradle file, 

Groovy:
```gradle
configurations.all {
    resolutionStrategy {
        force 'com.google.android.exoplayer:exoplayer:2.19.1'
    }
}
```

Kotlin:
```gradle
configurations.all {
    resolutionStrategy {
        force("com.google.android.exoplayer:exoplayer:2.19.1")
    }
}
```

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
    implementation 'com.cellrebel.android:cellrebel-sdk:1.9.65-rc2'
}
```

## Usage

Initialize SDK using your unique CLIENT_KEY string on application create:
```java
import com.cellrebel.sdk.CRMeasurementSDK;

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

## Demo project
https://github.com/cellrebel/CellRebelSDK/tree/master/demo
