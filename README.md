# CellRebel Android SDK

The **CellRebel CQoE SDK** allows your app to measure real-world mobile network quality and user experience with precision and control. The SDK runs only when you tell it to — ensuring zero background impact — and offers a GDPR-compliant API for user data removal. Designed to be lightweight and modular, it integrates easily into both Jetpack Compose and traditional View-based apps.

---

## 📦 Integration

### 1. Add CellRebel Maven Repository
**Project-level Gradle file**

**Kotlin DSL (`settings.gradle.kts`):**

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://raw.githubusercontent.com/cellrebel/CellRebelSDK/master/releases")
        }
    }
}
```

**Groovy (`settings.gradle` or `build.gradle` in project root):**

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://raw.githubusercontent.com/cellrebel/CellRebelSDK/master/releases"
        }
    }
}
```

---

### 2. Add SDK Dependency
**App-level Gradle file**

```groovy
dependencies {
    implementation "com.cellrebel.android:cellrebel-sdk:1.12.2"
}
```

---

### ⚠️ Upgrading to 1.10.1: ExoPlayer Version Check

> If your app already depends on **ExoPlayer**, make sure it is on version **2.19.0 or newer**. SDK 1.10.1 may cause issues when combined with older ExoPlayer versions.
>
> Either upgrade ExoPlayer to `2.19.0`+, or if you need to keep an older version, force it explicitly so it doesn't get bumped transitively:

```groovy
configurations.all {
    resolutionStrategy {
        force "com.google.android.exoplayer:exoplayer-core:2.18.1" // your pinned version
    }
}
```

---

### ⚠️ Upgrading to 1.12.x: Kotlin Version Check

> SDK 1.12.x depends on **`kotlin-stdlib` 2.4.20**, and Gradle will resolve your app's `kotlin-stdlib` to that version. If your app compiles Kotlin with an older Kotlin Gradle plugin (2.2 or earlier), the build can fail with an error like:
>
> `Module was compiled with an incompatible version of Kotlin. The binary version of its metadata is 2.4.0, expected version is 2.2.0.`
>
> Upgrade the Kotlin Gradle plugin to **2.3.0 or newer**. Apps written only in Java are not affected. Don't force `kotlin-stdlib` down to an older version: the SDK is built against 2.4.20 and may fail at runtime with an older one.

---

### ⚠️ AppCompat Required

> The SDK uses `AppCompatImageView`, so the `androidx.appcompat` dependency must be present.  
> **You can use any compatible version** — `1.0.0` or newer. Example:

```groovy
implementation "androidx.appcompat:appcompat:1.6.1"
```

This applies even to apps using only Jetpack Compose.

---

## 🚀 Usage

### 1. Initialization

**Kotlin:**

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CRMeasurementSDK.init(this, "YOUR_CLIENT_KEY")
    }
}
```

**Java:**

```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CRMeasurementSDK.init(this, "YOUR_CLIENT_KEY");
    }
}
```

---

### 2. Measurement Control

**Start measuring:**

```java
CRMeasurementSDK.startMeasuring(context);
```

- Call **after location permission dialog** (first launch)
- Then call in **MainActivity.onStart()** (subsequent launches). We give you a control over when our SDK can run, please make sure that `startMeasuring` is called at least once on every application launch. Feel free to place this call in other activities if that makes more sense to start our SDK from there instead.
- No data is collected until this is called
- Calling `startMeasuring` multiple times will not trigger multiple measurements and is safe to do. We have our internal mechanism that prevents measurements from running in parallel or too often. 

---

**Stop measuring (optional):**

```java
CRMeasurementSDK.stopMeasuring(context);
```

- Use during **high-load operations**
- Not required on app exit — SDK stops automatically

---

## 📡 Note about passive measurement collection

As of SDK version 1.9.40, a configuration refresh from CellRebel's web service is performed whenever the SDK internally references settings. Therefore, any attempted measurement, active or passive, will always first fetch a fresh configuration. This allows disabling of any or all measurement types through the CellRebel remote service, regardless of whether startMeasuring or stopMeasuring has been called.

This addresses a limitation in previous versions' ability to disable measurements, where configuration refresh was only performed by the SDK after startMeasuring was called, which led to passive measurements triggered by init to still be collected, even when startMeasuring was not called or even when stopMeasuring was called, since a cached configuration was instructing the SDK to continue with passive measurements.

---

## 🛠 Utility APIs

Get current SDK version:

```java
String version = CRMeasurementSDK.version(context);
```

Clear user data for GDPR "right to be forgotten":

```java
import com.cellrebel.sdk.workers.TrackingManager;

CRMeasurementSDK.clearUserData(context, new TrackingManager.OnCompleteListener() {
    @Override
    public void onCompleted(boolean success) {
        // Handle result
    }
});
```

---

## 💡 Demo App

Explore the [demo project](https://github.com/cellrebel/CellRebelSDK/tree/master/demo) for a full example.
