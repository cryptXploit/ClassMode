# ClassMode Production Release Protocol

Before uploading the final Application Bundle to the Google Play Console, you must strictly follow this security protocol.

## 1. Secure Production Keystore (.jks)
1. Open Android Studio > **Build** > **Generate Signed Bundle / APK...**
2. Select **Android App Bundle** and click Next.
3. Under Key store path, click **Create new...**
4. Store the .jks file securely outside the Git repository. Do not commit it.
5. Generate highly secure passwords for both the Keystore and the Key Alias. Store them in a password manager.

## 2. Environment Variable Configuration
Do not hardcode your keystore passwords in the gradle files. 
1. Open uild.gradle.kts and ensure the release signing config reads from System Environment Variables or a local keystore.properties file that is in .gitignore.

## 3. Mandatory Privacy Policy (Critical)
Because ClassMode utilizes ACCESS_BACKGROUND_LOCATION, the Play Store requires a rigorous privacy policy.
1. Host a Privacy Policy on a public website (e.g., GitHub Pages).
2. Explicitly state: "ClassMode collects location data in the background strictly to trigger automated Do Not Disturb sound profiles when you arrive at a designated campus, even when the app is closed or not in use."
3. Link this policy in the Google Play Console App Content section.

## 4. Generate the Final Signed .aab
1. Execute ./gradlew bundleRelease in the terminal.
2. Retrieve the .aab file from pp/build/outputs/bundle/release/.
3. Upload to the Google Play Console under the **Production** or **Internal Testing** track.
