# FEATURE BY FEATURE AUDIT

## 1. AUTOMATED SOUND SCHEDULING (TIME)
USER ACTION (Creates Schedule) -> UI (SchedulesScreen) -> ViewModel (ScheduleViewModel) -> DAO (ScheduleDao)
-> OS (SystemAlarmScheduler) -> AlarmManager.
**Status**: BROKEN. Works initially, but rebooting the device reactivates disabled schedules. Furthermore, the user's selected Sound Profile is ignored by RuleResolver.kt.

## 2. AUTOMATED SOUND SCHEDULING (LOCATION)
USER ACTION (Adds Classroom on Map) -> UI (LocationScreen) -> ViewModel (LocationViewModel) -> DAO (GeofenceDao)
-> OS (GeofenceManager).
**Status**: BROKEN. RuleResolver hardcodes location triggers to VIBRATE. 

## 3. MANUAL OVERRIDE
USER ACTION (Clicks Vibrate on Dashboard) -> UI -> ViewModel -> OS (SystemAudioController).
**Status**: WORKING. Successfully bypasses automation via ContextEngine.

## 4. FOCUS TIMER
USER ACTION (Starts Focus) -> UI (FocusScreen) -> ViewModel -> DAO (ScheduleDao generates SessionType.FOCUS) -> ContextEngine.
**Status**: WORKING. RuleResolver handles SessionType.FOCUS by forcing SILENT.

## 5. ALARM CLOCK
USER ACTION -> UI (AlarmsScreen) -> DAO -> AlarmManager -> AlarmReceiver -> AlarmRingingActivity.
**Status**: PARTIAL. Alarm UI is functional, but AlarmRingingActivity relies on system volume.
