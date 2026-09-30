# CLASSMODE — REGRESSION AND SIDE EFFECT AUDIT

## 1. PREVIOUS FIX: GEOFENCING INTEGRATION
### Original Problem
The standalone location picker was not triggering phone sound changes because it wasn't connected to the central automation engine.

### Current Implementation
`LocationViewModel` was modified to insert a dummy `ScheduleEntity` with type `CLASS` when a user creates a geofence.

### New Problem (Regression)
1. **Hardcoded Profiles**: Because it creates a `CLASS` type schedule, `RuleResolver.kt` hardcodes the response to `VIBRATE`. The user's default setting is ignored.
2. **Duplicate Workflows**: Users can now create a location via `LocationScreen` (which gives a nice map) OR via `SchedulesScreen` (which forces them to type Latitude and Longitude manually).

### Severity
**HIGH**
Evidence: `LocationViewModel.kt` (lines 35-46), `SchedulesScreen.kt` (lines 100-110).

## 2. PREVIOUS FIX: SYSTEM ALARM SCHEDULING
### Original Problem
Alarms were not triggering correctly or surviving reboots.

### Current Implementation
`SystemEventReceiver` was added to listen for `BOOT_COMPLETED` and `TIME_SET`. Upon triggering, it fetches all schedules and loops through them, passing them to `SystemAlarmScheduler`.

### New Problem (Regression)
**Zombie Alarms**: `SystemAlarmScheduler` does not check the `isEnabled` flag of a `ScheduleEntity`. On device reboot, **every disabled schedule is silently re-enabled** at the OS level (AlarmManager), causing the phone to switch to Silent/Vibrate for schedules the user explicitly paused.

### Severity
**CRITICAL**
Evidence: `SystemEventReceiver.kt` (lines 28-33) and `SystemAlarmScheduler.kt` (lines 25-35).

## 3. PREVIOUS FIX: FOCUS SCREEN UI
### Original Problem
The Focus Screen UI looked basic and didn't resemble modern aesthetics.

### Current Implementation
Rewritten to use an iOS-style `Canvas` circular progress bar, pure black background, and thin typography.

### New Problem (Regression)
**Broken Import (Hotfixed)**: The initial commit missed a `RoundedCornerShape` import which broke the Release build. It was hotfixed, but highlights the danger of large UI rewrites bypassing integration tests.

### Severity
**LOW** (Currently patched).

## 4. PREVIOUS FIX: BANGLA LOCALIZATION
### Original Problem
App lacked Bangla localization.

### Current Implementation
`values-bn/strings.xml` was created to hold the translated strings.

### New Problem (Regression)
**UTF-8 Corruption**: The Bangla string file was saved with incorrect encoding, converting every single character into question marks (`????????`). The localized version of the app is completely unreadable.

### Severity
**CRITICAL**
Evidence: `app/src/main/res/values-bn/strings.xml` (all lines).
