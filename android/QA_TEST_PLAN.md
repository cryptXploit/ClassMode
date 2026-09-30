# ClassMode Physical QA Test Plan

Welcome to the physical device handoff phase. The codebase has been fully scrubbed, fortified, and locked. You must now physically test the edge cases to guarantee OS-level resilience.

## 1. The Reboot Survival Test
**Goal:** Verify that Jetpack DataStore and SystemEventReceiver successfully recover state without user intervention.
1. Open ClassMode and tap **Focus Now** (or set a manual Vibrate override).
2. Physically restart the Android device.
3. Once the device boots up, unlock the screen.
4. **Verification:** Observe the physical sound profile. It should remain in Vibrate. Open the app; the Dashboard should clearly indicate that the Manual Override is still active.

## 2. The Process Death Test
**Goal:** Guarantee that exact OS-level AlarmManager intents survive aggressive memory reclamation.
1. Create a Time Schedule set to start exactly **2 minutes** from the current time.
2. Force close the app completely (swipe it away from the Recent Apps screen).
3. Lock the device screen and leave it on the desk.
4. **Verification:** Wait 2 minutes. The device must automatically shift to the targeted profile (e.g., Silent) and play the Haptic transition effect without the app being open.

## 3. The Geofence Reality Test
**Goal:** Confirm OS-level Play Services background location triggers.
1. Open the app and grant Allow all the time background location permission.
2. Create a Campus Schedule and tap the map to drop a pin on a location approximately 200 meters away. Set the radius to 100m.
3. Physically walk towards the perimeter.
4. **Verification:** Upon crossing the geofence perimeter, the device should automatically engage the scheduled profile. Walk out of the perimeter and verify it restores to Normal.

## 4. The Permission Denial Test
**Goal:** Validate the AutomationHealthMonitor fallback mechanisms.
1. Go to Android OS Settings > Apps > ClassMode > Permissions.
2. Manually revoke **Do Not Disturb Access** and **Alarms & Reminders**.
3. Open the ClassMode app and attempt to start a schedule or manual override.
4. **Verification:** The app must NOT crash. The Dashboard must immediately render a high-visibility Red Warning Card detailing the exact missing permissions.
