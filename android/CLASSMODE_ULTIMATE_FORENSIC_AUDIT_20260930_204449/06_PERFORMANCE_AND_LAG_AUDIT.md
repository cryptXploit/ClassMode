# PERFORMANCE AND LAG AUDIT

## 1. ContextEngine Polling
**Risk**: LOW. Uses Kotlin Flow combine perfectly to react to database changes without polling.

## 2. Map Rendering
**Risk**: HIGH (Static Risk). osmdroid MapView inside AndroidView is destroyed and recreated on orientation changes. selectedLocation state is lost during rotation due to lack of ememberSaveable.

## 3. Main Thread Database Access
**Risk**: LOW. Room DAOs are exclusively called via iewModelScope.launch or CoroutineScope(Dispatchers.IO).
