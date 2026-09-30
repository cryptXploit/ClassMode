# BUGS, ROOT CAUSES, AND MISSING FEATURES

1. **Bug**: User selects "Silent" in schedule, phone goes "Vibrate".
   - **Root Cause**: RuleResolver.kt ignores schedule.soundProfile and returns SoundProfile.VIBRATE hardcoded.
   
2. **Bug**: Disabled schedule activates after reboot.
   - **Root Cause**: SystemEventReceiver fetches all schedules on boot and calls SystemAlarmScheduler which lacks an isEnabled check.

3. **Bug**: App turns to gibberish when switched to Bangla.
   - **Root Cause**: Encoding failure in alues-bn/strings.xml resulting in ???????? for all strings.
