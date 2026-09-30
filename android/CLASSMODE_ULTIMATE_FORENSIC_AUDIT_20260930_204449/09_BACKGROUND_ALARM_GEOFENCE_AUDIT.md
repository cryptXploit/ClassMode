# BACKGROUND EXECUTION AUDIT

- AlarmReceiver handles exact time triggers.
- GeofenceReceiver handles location triggers.
- Both write to TriggerStateDao.
- **Flaw**: AutomationOrchestrator runs in ClassModeApplication scope. If the app process dies while in the background, geofence enter/exit events update the database, but the Orchestrator isn't running to actually change the phone's sound profile!
