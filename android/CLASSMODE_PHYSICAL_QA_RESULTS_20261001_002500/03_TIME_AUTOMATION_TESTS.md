# 03 - Time Automation Tests

## TEST 2 — TIME SCHEDULE
**Goal:** Verify time automation triggers OS audio changes. Do not assume success from UI.

**Procedure:**
1. Create temporary schedule: Profile = Silent, Condition = Time (2–3 mins in future).
2. Lock phone.
3. Wait for scheduled time.
4. Observe actual sound state (do not unlock).
5. Wait for schedule end.
6. Observe restoration.

**Evidence Record:**
* Expected Start Time:
* Actual Start Time:
* Audio State at Start:
* Expected End Time:
* Actual End Time:
* Audio State at End:
* Result (PASS/FAIL):

## TEST 5 — DISABLED SCHEDULE
**Goal:** Verify disabled schedules stay disabled (Phase 2).

**Procedure:**
1. Create a schedule 2 minutes in the future (Enabled = ON).
2. Disable it.
3. Wait beyond trigger time.
4. Expected: No automation occurs.
5. Reboot phone.
6. Verify it remains disabled.

**Evidence Record:**
* Did automation occur?: 
* State after reboot: 
* Result (PASS/FAIL): 

## TEST 6 — OVERNIGHT SCHEDULE
**Goal:** Verify modulo arithmetic boundary crossing.

**Procedure:**
1. Create a schedule spanning midnight (e.g. 23:58 to 00:03).
2. Save schedule.
3. Verify it does not immediately become invalid.
4. Wait for boundary transition.

**Evidence Record:**
* Saved successfully?: 
* Correct day selected?: 
* Automation fired accurately across boundary?: 
* Result (PASS/FAIL): 
