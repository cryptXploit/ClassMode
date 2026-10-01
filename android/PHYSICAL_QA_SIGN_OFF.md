# ClassMode Physical QA Sign-Off Ledger

**Tester Name:** ________________________
**Date of Testing:** ________________________
**Device Used (e.g., Pixel 7):** ________________________
**OS Version (e.g., Android 14):** ________________________

## Required Physical Test Matrix

- [ ] **The Navigation Test:** Verified 5-tab bottom navigation transitions smoothly without overlapping content or broken back-stack behavior.
- [ ] **The Global Haptic Test:** Verified deleting an alarm/schedule produces NO physical vibration when Haptics are OFF. Verified UI interactions and delete warnings trigger the physical device motor when Haptics are ON.
- [ ] **The Localization Rendering Test:** Changed Android OS System Language to Bangla (বাংলা). Verified Settings screen, Delete Confirmation Dialogs, and Alarm Ringing screens render native Bangla without text clipping, overlapping, or untranslated English placeholders.
- [ ] **The Destructive Safety Test:** Attempted to delete a Schedule, Location, and Alarm. Verified the localized Material 3 warning dialog explicitly blocks immediate deletion and requires confirmation.
- [ ] **The Background Automation Test:** Created an alarm or geofence. Locked the device, swiped the app from memory, and verified the OS still triggers the intended behavior (ringing or profile change) despite Doze/Process Death.

## Final Decision
- [ ] **APPROVED:** All tests passed. Codebase is cleared for Play Store rollout.
- [ ] **REJECTED:** Defects found. Rollback or hotfix required.

**Notes / Defects Observed:**
(Leave empty if none)

