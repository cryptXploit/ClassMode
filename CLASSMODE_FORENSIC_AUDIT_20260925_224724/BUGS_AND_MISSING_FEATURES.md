# ClassMode: Bugs and Missing Features

## 1. Issue Register

### ID: BUG-001
* **Severity:** HIGH
* **Category:** UI / UX
* **Affected Feature:** Geofence Location Selection
* **Exact Path:** `app/src/main/java/com/classmode/presentation/location/LocationScreen.kt`
* **Actual Behavior:** The `osmdroid` MapView renders, but tapping on the map does not update the Latitude/Longitude fields. Users must manually type coordinates.
* **Evidence:** The code explicitly states: `// But for this initial version, we rely on manual entry as fallback` and lacks an `Overlay` tap listener.

### ID: BUG-002
* **Severity:** MEDIUM
* **Category:** Validation Logic
* **Affected Feature:** Schedule Creation
* **Exact Path:** `app/src/main/java/com/classmode/presentation/schedules/SchedulesScreen.kt`
* **Actual Behavior:** The UI prevents saving a schedule if the End Time is numerically smaller than the Start Time.
* **Impact:** Users cannot easily create a schedule that spans across midnight (e.g., 10:00 PM to 2:00 AM).
* **Evidence:** `if (endMins <= startMins && endMins != 0) { Text("End time must be after start time") }`

### ID: BUG-003
* **Severity:** LOW
* **Category:** UI Completeness
* **Affected Feature:** Alarms Screen Dialog
* **Exact Path:** `app/src/main/java/com/classmode/presentation/alarms/AlarmsScreen.kt`
* **Actual Behavior:** The Add Alarm dialog uses rudimentary `+1 hr` and `+15 min` increment buttons instead of a native TimePickerDialog.
* **Evidence:** `OutlinedButton(onClick = { hour = (hour + 1) % 24 }) { Text("$hour h") }`

## 2. Unpolished States & Technical Debt
* **Language Support:** Although "Bengali" is an option in settings, strings are hardcoded in English throughout the Compose UI files.
* **Architecture Purity:** The Domain layer orchestrator runs as a Coroutine attached to the application context instead of a formal `Service` or `WorkManager` for the UI observation. (Though Geofencing and Alarms operate independently of the UI lifecycle).
