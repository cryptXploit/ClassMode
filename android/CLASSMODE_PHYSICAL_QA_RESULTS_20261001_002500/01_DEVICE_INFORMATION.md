# 01 - Device Information & Preparation

## Current Architecture Flow
Before testing, understand exactly how the app executes logic:
1. **Time Schedule**: User Action → SchedulesScreen UI → ScheduleViewModel → Room Database → SystemAlarmScheduler → OS AlarmManager → AlarmReceiver → RuleResolver → ContextEngine → AutomationOrchestrator → AudioManager (Actual device state).
2. **Geofence Schedule**: LocationScreen UI → GeofenceManager → OS Play Services → GeofenceReceiver → TriggerStateDao (Room) → RuleResolver → ContextEngine → AutomationOrchestrator → AudioManager.
3. **Manual Override**: Dashboard UI → DashboardViewModel → DataStore (Profile + Expiry) → ContextEngine (State Snapshot) → AutomationOrchestrator → AudioManager (SystemAlarmScheduler handles the expiry clearance).
4. **Reboot Recovery**: OS Boot → BOOT_COMPLETED Intent → SystemEventReceiver → Queries Room/DataStore → Re-schedules AlarmManager & Play Services.
5. **Alarm Clock**: Alarms UI → AlarmDao → SystemAlarmScheduler → AlarmManager → AlarmReceiver → AlarmRingingActivity (Full screen intent / Notification).

## Installation Verification
Execute the following in PowerShell/Terminal:
`ash
# 1. Build the APK
.\gradlew assembleDebug

# 2. Install to connected device
adb install -r app\build\outputs\apk\debug\app-debug.apk

# 3. Verify installation
adb shell pm list packages | findstr classmode
`

## Device Preparation Checklist
To accurately test the app, ensure the following permissions are granted via Android Settings (Settings > Apps > ClassMode > Permissions). Menu names vary by OEM (Samsung, Pixel, etc.).
- [ ] **Location**: Allow all the time (Required for Geofencing).
- [ ] **Alarms & Reminders**: Allowed (Required for exact time automation).
- [ ] **Do Not Disturb Access**: Allowed (Required to set phone to Silent/DND).
- [ ] **Notifications**: Allowed (Required for active session indicators).
- [ ] **Battery Unrestricted**: (Optional but recommended for test purity against Doze mode).

## Tester: Record Device Info Here
*   Date: 
*   Approximate time: 
*   Device model: 
*   Android version: 
*   App version: 
