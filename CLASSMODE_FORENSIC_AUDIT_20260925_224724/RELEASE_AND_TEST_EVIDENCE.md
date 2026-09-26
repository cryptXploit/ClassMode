# ClassMode: Release and Test Evidence

## 1. Build Artifacts Status

### Debug Build
* **Exists:** YES
* **File Path:** `app/build/outputs/apk/debug/app-debug.apk`
* **File Size:** ~13.4 MB
* **Compile SDK:** 34
* **Min SDK:** 26
* **Target SDK:** 34
* **Signing Status:** Signed with default Android debug.keystore.

### Release Build
* **Exists:** YES
* **APK Path:** `app/build/outputs/apk/release/app-release.apk`
* **AAB Path:** `app/build/outputs/bundle/release/app-release.aab`
* **File Size (APK):** ~3.8 MB
* **Signing Status:** The `build.gradle.kts` configuration looks for an environment variable `KEYSTORE_FILE_PATH`. Since this is an automated/isolated workspace, it defaults to whatever fallback is available. It is unlikely to be securely signed for the Play Store without the true developer providing the `.jks` file.

## 2. Test Evidence

### Unit Tests
* **Directory:** `app/src/test/java/com/classmode/domain/automation/`
* **Contents:** `RestoreStateManagerTest.kt` and `RuleResolverTest.kt`
* **Status:** These tests are present and target the critical domain logic governing automation safety. Previous builds indicate 8/8 tests pass.

### UI / Instrumented Tests
* **Directory:** `app/src/androidTest/`
* **Status:** No custom instrumented tests exist.

## 3. Real Device Execution Evidence
* **Status:** UNVERIFIABLE WITHOUT DEVICE TESTING. 
* While the code heavily utilizes real Android System APIs (`LocationServices`, `AudioManager`, `AlarmManager`), its execution on a physical device during this specific audit phase cannot be directly recorded (no emulator/device logs available).
