# CLASSMODE — MASTER FORENSIC REVIEW

## 1. WHAT WAS ACTUALLY BUILT
The ClassMode Android application is a system automation tool designed to toggle the user's physical device sound profile (Normal, Vibrate, Silent, DND) based on predefined rules like Time and Location. 

It uses a localized modern Android stack:
* **UI**: Jetpack Compose (Material 3)
* **Architecture**: Technically layered (Presentation/Domain/Data) but heavily coupled in practice.
* **Storage**: Room Database (Entities/DAOs) and SharedPreferences (DataStore is listed in dependencies but `PreferencesManager` and `RestoreStateRepository` use standard `SharedPreferences`).
* **Background**: `AlarmManager` for time scheduling, Google Play Services `GeofencingClient` for location triggers, and `BroadcastReceivers` to act as intermediaries.
* **Integrations**: `osmdroid` for mapping, Firebase Analytics/Crashlytics, and AdMob for monetization.

## 2. WHAT IS ACTUALLY WORKING (SOURCE/BUILD VERIFIED)
* **Local Persistence**: Room effectively stores Schedules, Geofences, and TriggerStates.
* **Manual Sound Override**: The dashboard successfully requests Android permission and sets the physical audio mode, effectively bypassing automation.
* **Basic Alarms**: The `AlarmReceiver` schedules and triggers basic system alarms. `AlarmRingingActivity` can launch a full-screen intent.
* **Focus Mode (UI)**: The Focus timer UI renders a countdown using an iOS-style circular design (re-written in a recent patch).

## 3. WHAT IS PARTIAL
* **Geofencing Integration**: Users can pick a location on a map (`LocationScreen`) or type coordinates manually (`SchedulesScreen`). However, state synchronization between the map UI and the core Schedule entity is fractured.
* **State Restoration**: `RestoreStateManager` works for simple overlapping sessions but breaks if a user expects a schedule to enforce "Normal" mode (the engine treats `NORMAL` as "no session" and drops it).

## 4. WHAT IS BROKEN (CONFIRMED DEFECTS)
* **Sound Profile Hardcoding**: The UI allows users to select a SoundProfile (e.g., Silent, Vibrate) for a schedule. **However, `RuleResolver.kt` completely ignores the database selection** and hardcodes `VIBRATE` for Class/Campus sessions, and `SILENT` for Exam/Focus sessions. The UI selection is dead code.
* **Ghost Alarms on Reboot**: `SystemEventReceiver` fetches *all* schedules on reboot (including disabled ones) and feeds them to `SystemAlarmScheduler`, which blindly schedules them without checking `isEnabled`. Disabled schedules will trigger.
* **Bangla Localization Corruption**: The file `app/src/main/res/values-bn/strings.xml` is corrupted (`????????`), rendering the Bangla translation completely unusable.
* **Overlapping UI Paths**: Creating a Location in the `LocationScreen` creates a hidden `CLASS` Schedule. Creating a Location schedule in `SchedulesScreen` requires manually typing Latitude/Longitude instead of using the map.

## 5. WHAT IS MISSING
* **DataStore Implementation**: Despite being in `build.gradle.kts`, it is unused.
* **Automated Testing**: Zero unit tests, integration tests, or UI tests exist in the source tree.
* **Production Release Artifact**: The `release` build type in Gradle falls back to a debug configuration because no physical `keystore.jks` exists in the repository.

## 6. HEALTH STATUS SUMMARY

| Area         | Current State | Evidence Level | Main Problem |
| ------------ | ------------- | -------------- | ------------ |
| Architecture | PARTIAL       | SOURCE VERIFIED | Domain rules bypass data layer state. |
| UI/UX        | PARTIAL       | SOURCE VERIFIED | Fragmented location flows; missing map in Schedules. |
| Scheduling   | BROKEN        | SOURCE VERIFIED | Disabled schedules turn on after reboot. |
| Geofencing   | PARTIAL       | SOURCE VERIFIED | Two conflicting registration systems. |
| Automation   | BROKEN        | SOURCE VERIFIED | RuleResolver ignores user profile choice. |
| Audio        | PARTIAL       | SOURCE VERIFIED | Cannot explicitly schedule "Normal" mode. |
| Testing      | MISSING       | SOURCE VERIFIED | No test implementations exist. |
| Localization | BROKEN        | SOURCE VERIFIED | UTF-8 corruption in `values-bn/strings.xml`. |
| Release      | BROKEN        | BUILD VERIFIED  | Keystore missing; falls back to debug. |
