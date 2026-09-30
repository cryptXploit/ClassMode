# 06 - Restoration Tests

## TEST 11 — RESTORATION SAFETY
**Goal:** Ensure RestoreStateManager does not maliciously overwrite user intent.

**Procedure:**
1. Start with phone in **Normal** mode.
2. Allow ClassMode time schedule to trigger **Silent** mode.
3. *While automation is active*, physically press volume keys to change phone to **Vibrate**.
4. Allow schedule to end.
5. Observe final state.

**Evidence Record:**
* Did ClassMode overwrite the user's manual in-session change? (Yes/No):
* Final state after session end:
* Result (PASS/FAIL):
