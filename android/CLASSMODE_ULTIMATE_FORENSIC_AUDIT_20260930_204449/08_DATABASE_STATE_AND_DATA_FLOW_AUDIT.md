# DATABASE, STATE, AND DATA FLOW AUDIT

- **Single Source of Truth Violation**: ScheduleEntity dictates schedule time, but TriggerStateEntity dictates if the schedule is currently "Active". This leads to race conditions if a trigger state is orphaned when a schedule is deleted.
- **Migration Safety**: No Room migrations are configured.
