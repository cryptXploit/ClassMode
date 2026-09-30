# Phase 1 to 15 Acceptance Matrix

| Phase | Claimed Goal | Current Implementation | Evidence | Status |
| ----- | ------------ | ---------------------- | -------- | ------ |
| 1 | Core Rule Correctness | RuleResolver accurately cascades User Override > Sleep > Exam > Class > Focus > Campus > Default. | RuleResolver.kt lines 20-40 correctly loops through priorities. | PASS (SOURCE VERIFIED) |
| 2 | Background Lifecycle | Ensure schedules respect isEnabled flag and clear triggers on disable. | SystemEventReceiver.kt line 34 filters if (schedule.isEnabled). | PASS (SOURCE VERIFIED) |
| 3 | Overnight Schedules | Allow overnight boundaries (e.g. 23:00 to 02:00) | ScheduleCalculator.kt properly modulo-calculates 24-hour overlaps and SchedulesScreen.kt validation was removed. | PASS (SOURCE VERIFIED) |
| 4 | Schedule Lifecycle | Deleting a schedule actively cancels OS intents. | ScheduleViewModel.kt invokes larmScheduler.cancelSchedule(entity.id) during deletion. | PASS (SOURCE VERIFIED) |
| 5 | Map UX | osmdroid map supports tap-to-select via MapEventsOverlay. | LocationScreen.kt instantiates overlay inside AndroidView factory. | PASS (SOURCE VERIFIED) |
| 6 | Alarms Time Picker | Native Android TimePickerDialog. | AlarmsScreen.kt launches TimePickerDialog via context. | PASS (SOURCE VERIFIED) |
| 7 | Localization | Hardcoded strings replaced with R.string. | strings.xml and alues-bn/strings.xml contain translated mapping. | PASS (BUILD VERIFIED) |
| 8 | Permission Safety | SecurityException caught during DND/Alarm setup without crashing. | SystemAudioController.kt and SystemAlarmScheduler.kt wrap OS calls in 	ry-catch. | PASS (SOURCE VERIFIED) |
| 9 | Default Profile Enforcement | Modifying Settings updates idle profile dynamically. | AutomationOrchestrator.kt detects defaultPreferenceChanged delta. | PASS (SOURCE VERIFIED) |
| 10 | Timer Safety | Manual overrides clear via OS AlarmManager instead of coroutines. | DashboardViewModel.kt computes expiry and posts to SystemAlarmScheduler. | PASS (SOURCE VERIFIED) |
| 11 | Override Reboot Safety | Override timer persists in DataStore and recovers on boot. | PreferencesManager.kt saves override_expiry_time and SystemEventReceiver.kt reinstantiates the clear action. | PASS (SOURCE VERIFIED) |
| 12 | Release Engineering | Secure ProGuard mapping for Enums/DAOs/Receivers. | proguard-rules.pro correctly shields com.classmode.domain.model.** and receivers. | PASS (BUILD VERIFIED) |
| 13 | Log Scrubbing | PII and Geo-location tracking logs removed from receivers. | grep verified no Log.d, println, or PII-leaking Log.i remains in receivers. | PASS (SOURCE VERIFIED) |
| 14 | Warning Eradication | GeofenceReceiver.kt unused variables cleaned up. | 	ransitionType deleted, compiling without warnings. | PASS (BUILD VERIFIED) |
| 15 | Physical QA Handoff | Generating testing plans and release protocols. | QA_TEST_PLAN.md and RELEASE_CHECKLIST.md are present in root. | PASS (SOURCE VERIFIED) |
