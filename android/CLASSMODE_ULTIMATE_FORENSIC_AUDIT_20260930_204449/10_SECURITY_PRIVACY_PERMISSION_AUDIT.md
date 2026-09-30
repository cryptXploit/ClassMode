# SECURITY & PRIVACY AUDIT

- **Permissions**: ACCESS_BACKGROUND_LOCATION is requested, which requires strict Play Store justification.
- **Exported Components**: SystemEventReceiver is exported but protected by Android system intent filters (BOOT_COMPLETED). 
- **Secrets**: No API keys exposed. AdMob uses the test ID ca-app-pub-3940256099942544~3347511713.
