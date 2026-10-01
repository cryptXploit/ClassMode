# UI/UX Physical QA Test Plan

## Overview
This document outlines the strict physical testing protocol required to validate the ClassMode UI/UX Modernization Phase. These tests must be executed manually by a human developer on a physical Android device to verify touch physics, gesture navigation, and system-level overrides.

## Required Test Cases

- **Test 1: The Haptic Toggle Test.** Go to Settings. Turn Haptics OFF. Tap on a Schedule card and navigate to Focus Mode. Verify absolute physical silence. Turn it back ON and verify the subtle click returns.
- **Test 2: The Destructive Intercept Test.** Attempt to delete a Schedule, Location, and Alarm. Verify the red Material 3 dialog intercepts the action. Tap "Cancel" and verify the item remains. Tap "Delete" and verify the item is destroyed.
- **Test 3: The Navigation Back-Stack Test.** Tap Dashboard -> Others -> Focus Mode. Use the physical Android system swipe-back gesture. Verify it correctly returns to the "Others" menu without closing the app or creating infinite loops.
- **Test 4: The Localization Rendering Test.** Change the Android OS System Language to Bangla (বাংলা). Open the app. Verify that the Dashboard empty state ("সব পরিষ্কার"), Schedule Delete Dialogs, and Settings toggles render without text clipping or overlapping elements.
