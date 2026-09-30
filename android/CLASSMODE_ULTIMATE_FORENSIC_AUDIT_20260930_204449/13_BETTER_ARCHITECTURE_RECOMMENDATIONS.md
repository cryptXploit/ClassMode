# ARCHITECTURE RECOMMENDATIONS

1. **Delete TriggerStateEntity**: Calculate active state dynamically based on system time rather than storing temporary boolean states in the database.
2. **Move Geofencing to WorkManager**: Stop relying on an Application-level CoroutineScope to listen to database changes. The GeofenceReceiver should directly invoke a lightweight Worker to evaluate rules.
3. **Merge Location UIs**: Remove manual Lat/Lon typing from SchedulesScreen and unify it with LocationScreen map picker.
