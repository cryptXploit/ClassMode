# ClassMode: Beginner Explanation

## 1. What exactly was built?
An Android application named ClassMode was built to automatically put your phone on Silent, Vibrate, or Do Not Disturb (DND) when you are in class. It uses your class schedule (times) and your physical location (geofencing) to determine when you are actually in class, and changes the phone's sound profile accordingly.

## 2. How the app currently works
The app's "brain" is very smart. When you create a schedule, it saves it to a database and sets an invisible alarm. When that alarm goes off, the app checks if you set a "Location Requirement" as well. If you did, it uses Google Play Services to draw an invisible circle around your classroom. Once your phone's GPS detects you are inside that circle during the scheduled time, it tells the phone to go Silent. When class is over, it checks if you manually changed the volume yourself. If you didn't, it safely restores your original volume.

## 3. What the user can actually do
* You can create weekly class schedules.
* You can pick a classroom location (by typing in Latitude and Longitude).
* You can set up normal Alarms that will ring and show a full-screen notification.
* You can manually override the app (e.g., hit a "Silent" button from the dashboard).

## 4. What does not work (or is very hard to use)
* **The Map:** There is a map screen for picking your classroom, but tapping on the map doesn't actually grab the location. You are forced to manually type in the Latitude and Longitude numbers, which is very frustrating for a normal user.
* **Overnight Schedules:** You cannot easily set a class that starts at 11:00 PM and ends at 2:00 AM because the app insists the end time must be "bigger" than the start time.
* **Adding Alarms:** The screen to add a normal alarm uses clunky "+1 hour" and "+15 minute" buttons instead of a normal scrolling clock.

## 5. What is completely missing
* There are no actual translations. Even though there is a "Bengali" button in settings, the app remains in English.
* There is no onboarding tutorial to explain how to get the location coordinates.

## 6. Why the app feels incomplete
The app feels like a powerful engine placed inside an unfinished car. The backend logic that handles the alarms, location tracking, and volume changing is extremely robust and well-written. However, the user interface (the buttons, maps, and forms you interact with) feels like a draft. A regular user would likely give up trying to use the Geofencing feature because they wouldn't know how to find their Latitude and Longitude. 
