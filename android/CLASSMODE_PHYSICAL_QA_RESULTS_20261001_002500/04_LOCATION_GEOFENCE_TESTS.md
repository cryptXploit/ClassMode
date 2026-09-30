# 04 - Location & Geofence Tests

## TEST 7 — GEOFENCE
**Goal:** Verify OS-level Play Services background location triggers.

**Architecture Note:** Requires ACCESS_BACKGROUND_LOCATION. Triggered by Geofence.GEOFENCE_TRANSITION_ENTER mapped to isLocationActive = true.

**Procedure:**
1. Grant background location permission.
2. Drop pin ~200m away (Radius 100m).
3. Walk outside -> inside -> outside perimeter.

**Evidence Record:**
* Configured Coordinates/Radius:
* Profile Before:
* Enter Time:
* Profile After Enter:
* Exit Time:
* Profile After Exit (Restoration):
* Result (PASS/FAIL):

## TEST 8 — TIME + LOCATION CONDITION
**Goal:** Verify boolean AND logic in RuleResolver.

**Procedure:**
Create a schedule requiring both Time AND Location.

**Evidence Record:**
| Condition | Expected Behavior | Actual Behavior | Result |
| --------- | ----------------- | --------------- | ------ |
| Case A: Correct time + Outside location | No activation | [ ] | |
| Case B: Wrong time + Inside location | No activation | [ ] | |
| Case C: Correct time + Inside location | Activation | [ ] | |

## TEST 9 / 10 — LOCATION ONLY / OR CONDITIONS
*Note: If the application logic supports 'Location Only' or 'Time OR Location', test those specific boolean resolution boundaries here.*
