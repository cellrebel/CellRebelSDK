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
    implementation "com.cellrebel.android:cellrebel-sdk:1.9.56"
}
```

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
String version = CRMeasurementSDK.getVersion();
```

Clear user data for GDPR "right to be forgotten":

```java
CRMeasurementSDK.clearUserData(context, new CRMeasurementSDK.OnCompleteListener() {
    @Override
    public void onCompleted(boolean success) {
        // Handle result
    }
});
```

---

## 💡 Demo App

Explore the [demo project](https://github.com/cellrebel/CellRebelSDK/tree/master/demo) for a full example.
