# ClassMode: Screen-by-Screen Inventory

## 1. DashboardScreen
* **Path:** pp/src/main/java/com/classmode/presentation/dashboard/DashboardScreen.kt
* **Reachability:** Reachable (Default start destination).
* **Contents:**
  * **System Warning Banner:** Shows location errors if permission denied.
  * **Current Status Card:** Displays active Sound Profile (Normal, Vibrate, Silent, DND) with animated scaling.
  * **Quick Actions:** Three buttons (Silent, Vibrate, Normal) to temporarily override the system.
  * **Active Schedule Card:** Displays the current running class or schedule type.
* **Status:** Fully functional. Interactions accurately update PreferencesManager.

## 2. SchedulesScreen
* **Path:** pp/src/main/java/com/classmode/presentation/schedules/SchedulesScreen.kt
* **Reachability:** Reachable (Bottom Navigation).
* **Contents:**
  * **List:** Shows existing schedules.
  * **FAB (Add):** Opens a dialog to create a schedule.
  * **Dialog:** 
    * Title text field.
    * Session Type filter chips.
    * Start/End Time buttons (opens actual TimePickerDialog).
    * Days of Week selector (circles S,M,T,W,T,F,S).
    * Sound Profile selector.
    * Condition selector (Time, Location, Both).
    * Latitude/Longitude text fields (if Location selected).
* **Status:** Functional, but manual Latitude/Longitude typing is required. The UI does not provide an integrated map picker for schedules.

## 3. LocationScreen
* **Path:** pp/src/main/java/com/classmode/presentation/location/LocationScreen.kt
* **Reachability:** Reachable (Bottom Navigation).
* **Contents:**
  * **List:** Displays registered geofences.
  * **Permission Banner:** Warns if fine location is denied.
  * **FAB (Add):** Opens AddGeofenceDialog.
  * **AddGeofenceDialog:**
    * osmdroid MapView (Rendered but NON-FUNCTIONAL for interaction).
    * Latitude, Longitude, Radius text fields.
* **Status:** Partially functional (UI-ONLY Map). Users must manually type their coordinates. The backend registration works correctly.

## 4. AlarmsScreen
* **Path:** pp/src/main/java/com/classmode/presentation/alarms/AlarmsScreen.kt
* **Reachability:** Reachable (Bottom Navigation).
* **Contents:**
  * **List:** Displays alarms with a toggle switch.
  * **Add Dialog:** Very simplified custom dialog with buttons to increment hour/minute.
* **Status:** Functional but unpolished UI.

## 5. AlarmRingingActivity
* **Path:** pp/src/main/java/com/classmode/presentation/alarms/AlarmRingingActivity.kt
* **Reachability:** Reachable (Triggered by SystemAlarmScheduler).
* **Contents:**
  * Full-screen overlay on lock screen.
  * "Alarm Ringing!" text.
  * Snooze / Dismiss buttons.
* **Status:** Fully functional. Successfully dismisses Android system notification and closes itself.

## 6. SettingsScreen
* **Path:** pp/src/main/java/com/classmode/presentation/settings/SettingsScreen.kt
* **Reachability:** Reachable (Bottom Navigation).
* **Contents:**
  * **Master Toggle:** Switch.
  * **Theme / Language:** Filter chips.
  * **Storage Repair:** Button to factory reset the Room database.
  * **Default Profile:** Filter chips for fallback ringer mode.
  * **Test Profile:** Buttons to physically test Normal, Vibrate, Silent, DND.
* **Status:** Fully functional.
