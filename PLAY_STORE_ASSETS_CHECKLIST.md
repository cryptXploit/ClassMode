# ClassMode - Release Asset & Data Safety Checklist

## 1. Graphic Assets
- [ ] **High-Res Icon:** 512px by 512px (PNG or WebP, up to 1MB). Must not contain shadows or corner radii (Google Play applies masking automatically).
- [ ] **Feature Graphic:** 1024px by 500px (PNG or WebP, up to 1MB). Keep critical text centered to avoid cropping on different screen sizes.
- [ ] **Phone Screenshots:** At least 4 screenshots (16:9 or 9:16 aspect ratio). 
    - *Required Views:* Dashboard (showing Active Profile), Timetable view, Automation Health Monitor, Settings (showing Privacy/Opt-in).
- [ ] **Tablet Screenshots (Optional but Recommended):** 7-inch and 10-inch screenshots to boost Play Store visibility.

## 2. Google Play Console: Data Safety Form
When filling out the Data Safety section in the Google Play Console, declare the following exactly as designed in our architecture:

*   **Data Collection & Security:**
    *   *Does your app collect or share any of the required user data types?* **Yes** (Due to AdMob and Opt-In Crashlytics).
    *   *Is all of the user data collected by your app encrypted in transit?* **Yes**.
    *   *Do you provide a way for users to request that their data be deleted?* **Yes** (Uninstalling clears all local data; Crashlytics tracking can be toggled off instantly).

*   **Data Types Declared:**
    *   **Crash Logs / Diagnostics:** Declared as *Collected*, *Optional* (Opt-in), used for *App Functionality/Analytics*.
    *   **Device or Other IDs (Advertising ID):** Declared as *Collected*, *Shared* (via AdMob), used for *Advertising or Marketing*.
    *   **Location:** If asked about Location data for Geofencing, clarify that it is processed **on-device only** and NOT collected off-device by the developer.
