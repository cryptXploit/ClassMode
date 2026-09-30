# ClassMode Living Project Context

**Date of Update:** 2026-10-01
**Git Branch:** main
**Latest Commit Hash:** f75c046a5af686368293a3fdafb5231896903c82

## Project Purpose
ClassMode is an Android application designed to automate physical device sound profiles (Normal, Vibrate, Silent, DND) based on a combination of exact time schedules, geofenced campus locations, and priority-based overrides (e.g., Exams > Classes > Sleep). 

## Current Architecture Summary
The application leverages a Unidirectional Data Flow (UDF) via Jetpack Compose and ViewModels. The core automation backend strictly relies on Clean Architecture, decoupling the UI from OS-level intents. Memory-leak-prone coroutines have been successfully replaced by deterministic OS AlarmManager exact timers and GeofencingClient triggers. States are persisted across Reboots and Process Death using Room Database (entities) and Jetpack DataStore (preferences).

## Navigation State
Currently, the UI routing is managed centrally in MainScreen.kt using Jetpack Navigation Compose (NavHost). 
The NavigationBar implements a strict 5-tab structure:
1. Dashboard (route: "dashboard")
2. Schedules (route: "schedules")
3. Location (route: "location")
4. Others (route: "others")
5. Settings (route: "settings")

*Note: Secondary features (Focus and Alarms) are fully nested inside the Others tab via OthersScreen.kt. The bottom bar elegantly handles deep-link selections by keeping the Others tab highlighted when actively inside these nested routes.*

## Theme/UI State
The application uses a complete MaterialTheme design system.
- **Colors:** Color.kt has been established with a professional, premium Light/Dark color palette (deep primary blues/purples and clean surface colors). Theme.kt supports these proper Light/Dark palettes and dynamic color injection.
- **Typography:** Type.kt has been established with a standard Material 3 Typography scale (display, title, body, label).
- **Components:** Basic standard Material 3 composables using the unified custom design system.
- **Shared Components:** Reusable DestructiveConfirmationDialog and EmptyStateView components are available in the components package for standardizing deletions and empty states across screens.

## Haptic Architecture
SystemHapticController provides two core OS-level feedback mechanisms:
1. performClickEffect(): A subtle tap for UI interactions.
2. performAutomationTransitionEffect(): A distinct waveform vibration for background automation transitions.
The haptic controller is now exposed to the Compose UI layer globally via LocalHaptic (CompositionLocalProvider in MainActivity.kt), allowing native access directly from UI components without passing it through every ViewModel.

## Completed Repairs
**Phases 1-15 (Core Automation Audit) are completely finalized.** The application reliably survives Process Death, handles SecurityExceptions, correctly prioritizes overrides, clears zombie alarms, scrubs PII from logs, and passes stringent physical QA scenarios regarding background execution logic.
- **Global Destructive Action Safety:** All primary screens (Schedules, Locations, Alarms) now require explicit confirmation before deletion.

## Outstanding UI/UX Issues
- SchedulesScreen delete safety and empty state modernization is complete. Delete operations in other screens (like Locations or Alarms) are instantaneous with no DestructiveConfirmationDialog.
- Unify the UI Design System (Colors, Typography, standardized Cards).
- Complete Bengali string localization for remaining nested UI elements.







