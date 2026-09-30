# 07 - Permission Failure Tests

## TEST 13 — DND PERMISSION REVOCATION
**Goal:** Verify graceful degradation instead of SecurityException crashes.

**Procedure:**
1. Grant DND access. Verify operation.
2. Revoke DND access in OS settings.
3. Trigger DND automation.

**Evidence Record:**
* Did the app crash?:
* Did a warning appear on the Dashboard (AutomationHealthMonitor)?:
* Did the app safely fallback/bypass?:
* Result (PASS/FAIL):

## TEST 14 — EXACT ALARM PERMISSION
**Goal:** Verify Android 12+ SCHEDULE_EXACT_ALARM requirements.

**Procedure:**
1. Revoke Exact Alarms permission in OS Settings.
2. Attempt to schedule a time trigger.
3. Observe UI response.

**Evidence Record:**
* UI Response/Error handling:
* Result (PASS/FAIL):
