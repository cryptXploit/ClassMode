# ClassMode Physical QA Test Plan

1. **The Navigation Test:** Verify 5-tab bottom navigation transitions smoothly without overlapping content or broken back-stack behavior.
2. **The Global Haptic Test:** Go to Settings -> Turn Haptics OFF. Verify deleting an alarm/schedule produces NO physical vibration. Turn Haptics ON. Verify UI interactions and delete warnings trigger the physical device motor.
3. **The Localization Rendering Test:** Change Android OS System Language to Bangla (বাংলা). Open the app. Verify Settings screen, Delete Confirmation Dialogs, and Alarm Ringing screens render native Bangla without text clipping, overlapping, or untranslated English placeholders.
4. **The Destructive Safety Test:** Attempt to delete a Schedule, Location, and Alarm. Verify the localized Material 3 warning dialog explicitly blocks immediate deletion and requires confirmation.
5. **The Background Automation Test:** Create an alarm or geofence. Lock the device, swipe the app from memory, and verify the OS still triggers the intended behavior (ringing or profile change).
