# ClassMode: Feature & Architecture Map

## Feature Completeness Matrix

| Feature / Screen | UI Exists | Logic Exists | Persistent Data | Android API Integrated | Background Support | Tests | Actual Status | Evidence |
|---|---|---|---|---|---|---|---|---|
| Schedule Creation | YES | YES | Room DB | AlarmManager | YES | NO | IMPLEMENTED AND SOURCE-VERIFIED | ScheduleViewModel |
| Geofencing | YES | YES | Room DB | GeofencingClient | YES | NO | PARTIAL (UI lacks map tap) | GeofenceManager |
| Sound Automation | YES | YES | SharedPreferences | AudioManager | YES | YES | IMPLEMENTED AND SOURCE-VERIFIED | SystemAudioController |
| App Reboots | NO UI | YES | Room DB | BOOT_COMPLETED | YES | NO | IMPLEMENTED AND SOURCE-VERIFIED | SystemEventReceiver |
| Custom Alarms | YES | YES | Room DB | AlarmManager | YES | NO | IMPLEMENTED AND SOURCE-VERIFIED | SystemAlarmScheduler |
| Haptic Feedback | NO UI | YES | N/A | Vibrator | N/A | NO | IMPLEMENTED AND SOURCE-VERIFIED | SystemHapticController |

## Application Architecture
The application strictly follows Clean Architecture guidelines.

\\mermaid
graph TD
    A[Presentation: Compose UI + ViewModels] --> B[Domain: ContextEngine, Orchestrator]
    B --> C[Data: Room DAOs, Repositories]
    B --> D[System Integrations]
    D --> E[AudioManager, AlarmManager, GeofencingClient]
\
## Data Persistence & Automation Flow
\\mermaid
graph TD
    U[User creates Schedule] --> DB[(Room Database)]
    DB --> CE[ContextEngine observes Schedules]
    DB --> AM[SystemAlarmScheduler schedules Exact Alarms]
    AM --> RCV[AlarmReceiver receives intent]
    RCV --> TS[Updates TriggerStateDao]
    TS --> CE
    CE --> AO[AutomationOrchestrator resolves Profile]
    AO --> SAC[SystemAudioController applies Profile]
\