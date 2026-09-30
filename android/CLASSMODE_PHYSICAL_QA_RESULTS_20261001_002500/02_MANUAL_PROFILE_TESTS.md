# 02 - Manual Profile Tests

## TEST 1 — BASIC MANUAL PROFILE
**Goal:** Establish baseline device interaction. Verify AudioManager correctly translates UI intents.

**Procedure:**
1. Open ClassMode.
2. Ensure no active class schedules are running.
3. Tap each Quick Action profile on the Dashboard.
4. Verify the actual physical device ringer state (using volume keys/status bar).

**Evidence Record:**
| Profile | UI Action | Expected Device State | Actual State | PASS/FAIL | Notes |
| ------- | --------- | --------------------- | ------------ | --------- | ----- |
| Normal  | Tap Card  | Ringer ON / Normal    | [ ]          |           |       |
| Vibrate | Tap Card  | Vibrate Only          | [ ]          |           |       |
| Silent  | Tap Card  | Silent / DND          | [ ]          |           |       |

## TEST 3 — MANUAL OVERRIDE TIMER
**Goal:** Verify OS-level timer correctly clears the override (Phase 10).

**Procedure:**
1. From Dashboard, trigger "Focus Now (1h)" or equivalent manual timer (if implemented, otherwise manually test standard override). 
2. Record start state.
3. Wait for expiry.
4. Verify restoration.

**Evidence Record:**
* Selected Profile: 
* Expiry Duration: 
* Actual Start State: 
* Actual Expiry Time: 
* State After Expiry: 
* Dashboard State: 
* Result (PASS/FAIL): 
