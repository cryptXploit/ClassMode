# 05 - Reboot & Process Death Tests

## TEST 4 — REBOOT SURVIVAL
**Goal:** Verify OS BOOT_COMPLETED Intent triggers SystemEventReceiver to reconstruct alarms and DataStore overrides.

**Procedure:**
1. Set an active manual override. Set an upcoming time schedule.
2. Restart physical phone.
3. Unlock, wait for boot processing.
4. Check Dashboard. Wait for future trigger.

**Evidence Record:**
| Item | Before Reboot | After Reboot | Result |
| ---- | ------------- | ------------ | ------ |
| Schedule enabled | [ ] | [ ] | |
| Override active | [ ] | [ ] | |
| Sound state | [ ] | [ ] | |
| Future trigger fired | [ ] | [ ] | |

## TEST 12 — PROCESS / RECENT-APPS TEST
**Goal:** Differentiate between OS memory reclamation and user-invoked Force Stops.

**Procedure:**
1. **Remove from Recents:** Schedule automation 2 mins out. Swipe app away. Lock phone.
2. **Force Stop:** Schedule automation 2 mins out. Settings > Apps > ClassMode > Force Stop. Lock phone.

**Evidence Record:**
| Test | Trigger Worked? | Expected Difference | Result |
| ---- | --------------- | ------------------- | ------ |
| Remove from Recents | [ ] | AlarmManager fires (OS Intent). | |
| Force Stop | [ ] | Alarms are stripped by Android OS explicitly (App dead). | |

*Note: Android OS intentionally drops PendingIntents when an app is Force Stopped via Settings. This is intended behavior. The swipe-away test is the critical path for memory reclamation.*
