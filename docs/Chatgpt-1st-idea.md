# ClassMode — Student Focus & Smart Silent Ecosystem

তোমার আইডিয়াটা আসলে শুধু একটা ফোন Silent করার অ্যাপ না। এটাকে এমন একটা Android-first productivity SaaS হিসেবে তৈরি করা যায়, যেটা শিক্ষার্থীদের ক্লাস, পরীক্ষা, লাইব্রেরি, পড়াশোনা ও ঘুমের সময় ফোনের distraction কমাবে।

তোমার মূল আইডিয়ায় তিনটি জিনিস আছে:

1. Location অনুযায়ী ফোন Silent/Vibrate করার automation।

2. নির্দিষ্ট সময় অনুযায়ী স্বয়ংক্রিয়ভাবে ফোনের Sound Mode পরিবর্তন।

3. শিক্ষার্থীদের জন্য এমন একটি Focus ecosystem, যেখানে এগুলো একসঙ্গে কাজ করবে।

আমার প্রস্তাব হলো, প্রথমে একটি সম্পূর্ণ offline-capable Android app বানাবে, তারপর user demand অনুযায়ী cloud, campus এবং institutional features যোগ করবে। শুরু থেকেই বড় server, database বা AI infrastructure দরকার নেই।

নিচে competition, technical limitations, product differentiation, monetization, architecture, development roadmap এবং Google Play publication—সব একসঙ্গে আলোচনা করছি।


# 1. Competition ও market reality

প্রথমে একটা গুরুত্বপূর্ণ বিষয়: শুধু location ও time অনুযায়ী ফোন Silent করার অ্যাপ নতুন category নয়। এই ধরনের automation আগে থেকেই আছে। তাই শুধু একই feature সুন্দর UI দিয়ে বানালে long-term competitive advantage তৈরি হবে না।

## সরাসরি প্রতিযোগী

![Auto Silent Scheduler for Android - Download the APK from Uptodown](https://images.openai.com/static-rsc-4/RorOABF55mawn7btpTA6mPwe-bwNL6VZCM-iAn-W9Cs5l0sWqPndpWaKmJFqltxO7MOdCCHOMcBWc5TKlw1_DvOOd9reRSWONDJzQxJ-IC4bdTdgOYbtEj4_0Zm1dhCzsF7tU2NcZ1cIWH_daCXO7t-GMUBV6zgVydKfmQeJs80?purpose=inline)

Auto Do Not Disturb

Direct competitor

Location, time, calendar, Wi-Fi, Bluetooth দিয়ে Silent, Vibrate এবং DND automation করতে পারে। Temporary silence ও previous settings restore করার সুবিধাও আছে।

![](https://www.google.com/s2/favicons?domain=https://play.google.com\&sz=32)

Apps on Google Play

![Télécharger MacroDroid (gratuit) Android - Clubic](https://images.openai.com/static-rsc-4/MIj74kPd8f2Knb-TRFJ184fhGtG_zsfOTNzdpfb-QgYzYRHDeg4HXJwT50FnM2CfCgvAcNbbPede2lTkdq-usF4SxPGv23sOo4WdumqQm-urxUTgl4uja-0oveF0LURH8bJkV0fJYCTBnyKdYoVg6deQWA2bJ1hX9jR5ngynnlo?purpose=inline)

MacroDroid

Indirect competitor

Trigger-condition-action automation, templates ও community macros দিয়ে ব্যবহারকারীরা একই ধরনের workflow তৈরি করতে পারেন।

![](https://www.google.com/s2/favicons?domain=https://unstore.io\&sz=32)

Unstore

![\[U OS\]Modes and Routines - Galaxy Store 上的應用程式](https://images.openai.com/static-rsc-4/5rHIf8awm4gd_zSfQlnbsgoBsNmH5ctDSinKRXDXIOczp6QOmqrltSeUm9ooD8xTOwA9tOo1SGh6dCPjMJveID6vCNfoYeqxo1Y7oF7n9FZF0Jh0wF0sSLz_HxDTYs8j87XLkozagCIj-osFMaPHewXuVOtomXaTPKGBTepJ_zQ?purpose=inline)

Samsung Modes and Routines

Built-in alternative

Samsung-এর compatible ফোনে আলাদা app ছাড়াই sound, app ও location-based routines ব্যবহার করা যায়।

![](https://www.google.com/s2/favicons?domain=https://app-tips.org\&sz=32)

AppTips Forum

![Slash - Free maps and location icons](https://images.openai.com/static-rsc-4/DjSRTgQ9ALwEGoJwXdKabb5nfVqJp0VTiZ1hz_0Pgc-WjK_ivhKLaMXy7deYz-0lI7ijWrGMYvXZNqWuxxdSLOIq87wQfmoXA5dgKeYJ8Z1FS9GBTmQnE2uJm51j_S4eiWNz8D0wpIUeIk9RsU38qrMO100V6WhsV57P7NmJwoA?purpose=inline)

GeoMute: Location Silencer

Closest use case

Google Play-তে location, time ও context অনুযায়ী Ring, Vibrate, Silent automation-এর বর্ণনা রয়েছে।

![](https://www.google.com/s2/favicons?domain=https://play.google.com\&sz=32)

Apps on Google Play

আরও একটি Mute Marker নামে location-based silence product রয়েছে, যার নিজস্ব website-এ school, college ও office ব্যবহারের কথা বলা হয়েছে।

![](https://www.google.com/s2/favicons?domain=https://chetanaai.com\&sz=32)

ChetanaAI

এটি ২৪ সেপ্টেম্বর ২০২৬ পর্যন্ত পাওয়া web evidence-ভিত্তিক competitor overview। সম্পূর্ণ Play Store market-share বা competitor revenue audit নয়।

## তাহলে তোমার অ্যাপের novelty কোথায় হবে?

Novelty-কে তিনটি স্তরে ভাবো।

|
স্তর

|

তোমার পণ্য

|
| --- | --- |
|

Basic utility

|

Location/time অনুযায়ী Silent করা

|
|

Product differentiation

|

Student timetable, lecture schedule, exam mode, smart restoration

|
|

Ecosystem differentiation

|

Campus schedules, teacher/classroom tools, shared timetable, institution-level integrations

|

আমার প্রস্তাবিত positioning:

> A student-first, privacy-first smart focus companion that automatically protects classroom silence and helps students stay focused throughout their academic day.

অর্থাৎ, এটি generic automation app নয়। শিক্ষার্থীদের দৈনন্দিন academic routine-এর জন্য একটি সহজ, নির্ভরযোগ্য system।

মূল বাজারের hypothesis: শিক্ষার্থীরা এমন একটি app চাইতে পারে যেটি একবার class routine সেট করার পর নিজে থেকেই কাজ করবে, প্রতিদিন manual Silent করতে হবে না, আর ক্লাস শেষে ফোন আবার আগের অবস্থায় ফিরে আসবে।

এটি এখনো hypothesis। বাস্তবে ২০–৩০ জন শিক্ষার্থীর সঙ্গে পরীক্ষা করে দেখতে হবে তারা সত্যিই এটি ব্যবহার করতে চায় কি না।


# 2. Product design: অ্যাপটিকে কীভাবে সম্পূর্ণ ecosystem বানাবে

প্রথমে একটি working name রাখি: ClassMode। এটি final brand name নয়; domain, trademark এবং Play Store name availability পরে যাচাই করবে।

![FocusGuard — Block Reels, Shorts & Doomscrolling. Get Your Life Back.](https://images.openai.com/static-rsc-4/euzlA0G31Z3JY9GcPUoPexiuq9SuhEEJ7KFN782EonaRbDVOJZA-grY4VCOYkibbJYFvBhn--abfPXDwAxqIEG_Dgzvs2YLLLDvDtq-on_PirsfzvKh7ZUqlhrhujVsrnXkt2dFdfgWN_MqfmqmdcOrYZZ_A01HGM5dPEuyWXGY?purpose=inline)

Design inspiration: একটি clean, student-first mobile dashboard; এটি generated concept reference, final product UI নয়।

## Product-এর ৫টি core module

01 — Smart Campus Mode

শিক্ষার্থী তার university, college, classroom বা library location সেট করবে। নির্ধারিত জায়গায় প্রবেশ করলে Silent/Vibrate/DND profile চালু হবে এবং বের হলে আগের sound setting restore হবে।

Core MVP

02 — Smart Class Timetable

Sunday থেকে Thursday বা নিজের class days, subject, start/end time, room এবং break schedule সেট করা যাবে। প্রতিটি class-এর জন্য আলাদা sound profile থাকবে।

Core MVP

03 — Focus, Study & Sleep Modes

Library study, exam, self-study, sleep, prayer বা meeting-এর মতো custom profile। Duration শেষ হলে আগের setting restore হবে।

Core / Phase 2

04 — Campus Community

Shared class routine, campus-specific timetable templates, student clubs এবং verified institution-এর published schedules।

Phase 3

05 — Academic Focus Dashboard

Class countdown, upcoming lecture, today's schedule, focus sessions, missed automation alerts এবং weekly focus statistics।

Phase 2

## 2.1. Smart Campus Mode — মূল feature

এই feature-এর UX হবে এমন:

![Quels sont les Lieux et comment puis-je les configurer? – GeoZilla](https://images.openai.com/static-rsc-4/QVXiCj4zJVmO3aNxqRqhfvCPDhaoKqkqabVPWRF54hQki4mpHxru3277d95ldeipegJe55fzWdJfK4OuxjGGeMuF7tieMlx_P125-gUiijEbEjRZxDuRogCLcqcFX92l9dvLw4KTfSqx1SuZPyKLHeg85SSIG5vtiG1ibUUQI-Y?purpose=inline)

![This forgotten Pixel 10 feature just made my life a little bit easier — Here's how to use it | Android Central](https://images.openai.com/static-rsc-4/wOuQLouCIPG4eCREpJjm0NEcMbPmat2ib_SPW0IouLsGWbSfAFLy0JHOnOvHYCML_VI4B1kwZYCjOj24FhsXMivX_Y_n7NLgnklSJcGGWn2EbGIr7XC1ipfCjR2frSsWsAhmRgPnvusxYjUtWGWpdq9nXQPvSMR1mciCRCLtDUA?purpose=inline)

![How to create a rule to silence your Google Pixel phone based on Wi-Fi or location - TechRepublic](https://images.openai.com/static-rsc-4/3cR4r5gjPs1EInuZxLqEfndPAh3151PA56EjGpg9fKIuzeVaArv1a6TBxDgyqdRymXqlYIO-enKiTeds_XBnA_fFhJP4lifCUkVcc9JP43zXVU9Wq7eY2HrNA26bn1F5nTVaJlnkSzt8RBjb5gUYDOJL-iLNkr1OMnHPxi9nAzc?purpose=inline)

ধরো, একজন শিক্ষার্থী Rajshahi University-তে পড়াশোনা করে।

সে একবার:

* Campus location নির্বাচন করবে।

* Campus geofence radius 150–300 মিটার দিয়ে শুরু করবে।

* Campus-এ প্রবেশের পর Vibrate অথবা DND চালু করার নিয়ম দেবে।

* Campus থেকে বের হলে পূর্বের setting restore করার নিয়ম দেবে।

এখানে একটি গুরুত্বপূর্ণ product decision: পুরো university campus-কে সবসময় Silent না করে আলাদা classroom বা lecture timetable-এর সঙ্গে location combine করবে।

উদাহরণ:

|
Context

|

Action

|
| --- | --- |
|

Campus-এ প্রবেশ

|

Optional Vibrate

|
|

নির্ধারিত lecture + campus

|

Silent/DND

|
|

Lecture শেষ

|

আগের sound mode

|
|

Library study

|

DND, user-selected duration

|
|

Campus থেকে বের হওয়া

|

Active campus profile বন্ধ

|

এতে campus-এর বাইরে যাওয়া-আসা করা শিক্ষার্থীদের জন্যও কাজ করবে।

### Location + timetable একসঙ্গে কীভাবে কাজ করবে?

একটি rule engine তৈরি করবে:

```
WHEN
  (Inside selected classroom geofence)
  AND (Current time is within class schedule)
  AND (Automation is enabled)

THEN
  Activate the user's selected sound profile

WHEN
  Class ends OR user exits geofence
THEN
  Restore the saved previous profile
```

সবচেয়ে গুরুত্বপূর্ণ হলো restoration। ধরো, শিক্ষার্থী আগে থেকেই নিজের ফোন Vibrate করে রেখেছে। ClassMode চালু হয়ে Silent করল। ক্লাস শেষে সরাসরি General করে দিলে তার original preference নষ্ট হবে।

তাই app-কে আগেকার state সংরক্ষণ করতে হবে এবং শুধুমাত্র নিজের পরিবর্তন করা state নিরাপদভাবে restore করতে হবে।

## 2.2. Timetable automation

এটিকে শুধু alarm scheduler বানাবে না। একটি recurring schedule engine বানাবে।

![Penmark](https://images.openai.com/static-rsc-4/-xZeM-yqsCkuK_u21J40uCx8eHC0Q3Kd6Zh3c1b8rc5RL3PdB5OqKYUorcBJzxY88SU8ugaQppAwSI7tfULm3Dv8hLdcRbzP2i8LOB6AGlrs6eAFB1lyC94P9F8-_Q-VLtfhUSVoe3MPU_Wk1amDalAxgktQ6OLjlYkOtP9JBMs?purpose=inline)

![Web site created using create-react-app](https://images.openai.com/static-rsc-4/5QDj_66yz0NlpDesJPttZFPPS5z33n2ehFsn5Tq3kzTB1Dc7QSr_UL-6kL5RaqUXmuD4WdMD5tH7m45NAWZcrB5R0n_lVDBBwu1OLtoVHnd2Nsne24LUMSHn6SEAis5iRsP-LgWT6iQ1fhyI6_XHJW02CFCC0G1P1YF4g_WBtR8?purpose=inline)

![Download Saturn Calendar App \[Updated Sep 24\] | WorldsApps](https://images.openai.com/static-rsc-4/rIhJQMrNEo0vRwFfMCU8Hzw0qfYg5mCmelEfkTI_qSWD_DddEn-FgUU-Gsaplr1EyaX6uZLtuq_kFER66EZt_TN_4aVaJoRgw4pNleL3r3hl1xlqvog9xrF9g3vHsJji2111pzlaqpChx04ikxJy0L9pFwSZwZhJVSRQ4zfZ3yw?purpose=inline)

6

যে ফিচারগুলো থাকবে:

* Recurring weekly timetable।

* Subject, room, teacher name ও class time।

* একাধিক class একই সময়ে থাকলে conflict warning।

* Holiday, exam week, semester break।

* Class cancel বা reschedule করার option।

* Manual override: “এই class-এর জন্য automation বন্ধ করো”।

* Quick action: আগামী ৩০ মিনিট Silent।

* Timezone ও daylight saving support, যাতে international students-দেরও সমস্যা না হয়।

একটি smart feature: শিক্ষার্থী একবার timetable-এর ছবি বা PDF থেকে schedule import করতে পারবে। প্রথমে manual entry থাকবে; OCR/import পরের phase-এ করা যাবে।

OCR পুরোপুরি local রাখলে server cost কমবে, আর timetable cloud-এ পাঠানোর প্রয়োজন হবে না।

## 2.3. Student Focus Dashboard

শুধু Silent automation করলে শিক্ষার্থী দিনে একবারও app খুলতে পারে না। সেটি app-এর retention-এর জন্য সমস্যা।

তাই dashboard-এ দৈনিক প্রয়োজনীয় utility থাকবে:

![Focusplan —](https://images.openai.com/static-rsc-4/lkSg6o_443czl_uIx5AEuYQd1aX_c3OwndKPCql1bBxHgsrhCINCcafbKXFZu7VW02lhyrR58eZmQKnxhjnJYca84zPKtdSenQJSg4rgM1oVVzBnTXdswae-z-sALlVICekdWmc0QQDiNy9n0N2IqikeGbLq9-R5BU5KqOvhz_8?purpose=inline)

![OverlayFocus - Block Distractions. Stay Focused. Achieve More.](https://images.openai.com/static-rsc-4/tPxkdn7ityjmlV8kZWyMix1YOXsxyx3TAmuaHItgxpn-QXvJsEq0tqCTwqyBC0iUBI0sX8aIhV3vt7YWYjsBtJtzzih3-s06JhvoHWxTQPaZ6-hp2yNOyS7LL21VknIgVt3NMy3NSo-vnJzP5WKR0cHJkjhHuHps-sONQNo0rdY?purpose=inline)

![Minuteur Étude Aesthetic — Belle app de minuteur pour étudier | Focus Now](https://images.openai.com/static-rsc-4/z4E8wpouAqlTZkIBOdvuQD81RHwP2VrXvt3N59GU7xZ_OB790zv8uqRntdxt2Ctor3V5HsEAtUgG07BLaf3pRuRYoQWQJU_7Xx6qfkIRgohuLQuuYd3QgifoIgw8tPnkEXJfKodwbUDP2TzahKV4wvrHzZicbPnMJZmkomadP0I?purpose=inline)

5

|
Feature

|

কী করবে

|
| --- | --- |
|

Next Class

|

পরবর্তী ক্লাসের সময় ও countdown

|
|

Focus Timer

|

25/50/90 মিনিটের focus session

|
|

Today's Schedule

|

আজকের সব ক্লাস ও break

|
|

Quiet Time Summary

|

কতক্ষণ focus profile active ছিল

|
|

Missed Automation

|

Permission বা system restriction-এর কারণে rule কাজ না করলে জানাবে

|
|

Quick Silent

|

এক ট্যাপে temporary mode

|

Focus timer-এর সময় ফোনের sound mode পরিবর্তন ঐচ্ছিক থাকবে। ব্যবহারকারী নিজের পছন্দ অনুযায়ী শুধু notification suppression, Silent বা Vibrate বেছে নেবে।

# 3. যেসব feature তোমাকে আলাদা করতে পারে

এখানে আমি feature-গুলোকে development cost এবং সম্ভাব্য user value অনুযায়ী সাজিয়েছি। এগুলো market-tested demand বা নিশ্চিত competitive advantage নয়; validation করার জন্য product hypotheses।

Tier 1 · MVP differentiation

Smart Restore & Conflict Resolver

দুইটি automation একসঙ্গে চালু হলে কোনটি কার্যকর হবে, সেটা নির্ধারণ করবে।

যেমন, Sleep Mode active থাকা অবস্থায় class schedule শেষ হলেও ফোনকে হঠাৎ General করবে না।

Implementation: local rule engine, priority, saved state, event history। Server প্রয়োজন নেই।

Tier 1 · MVP differentiation

Automation Health Monitor

User-কে দেখাবে:

* Location permission active কি না।

* DND access দেওয়া আছে কি না।

* Battery optimization-এর কারণে background task সীমাবদ্ধ কি না।

* Last successful automation কখন হয়েছে।

এটি অত্যন্ত গুরুত্বপূর্ণ, কারণ automation কাজ না করলে user app-টির ওপর বিশ্বাস হারাবে।

Tier 2 · Student ecosystem

Campus Schedule Templates

University, department, semester এবং section অনুযায়ী timetable template তৈরি ও share করা যাবে।

Student চাইলে QR code বা shareable link দিয়ে class routine import করবে।

Template public হতে পারে, কিন্তু personal location, device status বা actual attendance কখনো public হবে না।

Tier 2 · Retention

Focus Streak & Study Insights

Focus session এবং scheduled quiet periods-এর aggregate statistics। শিক্ষার্থী নিজের weekly progress দেখতে পারবে।

কাউকে কতক্ষণ ফোন Silent রাখতে হবে এমন বাধ্যবাধকতা থাকবে না। User চাইলে analytics বন্ধ করতে পারবে।

Tier 3 · Institutional SaaS

Campus Admin Portal

Institution-এর অনুমোদিত প্রতিনিধি timetable templates publish করবে, semester dates আপডেট করবে এবং campus schedule manage করবে।

Institution-এর dashboard-এ কোনো শিক্ষার্থীর live location, ব্যক্তিগত device state বা individual focus history দেখানো হবে না।

## আমার মতে সবচেয়ে গুরুত্বপূর্ণ product moat

একটি feature সহজেই অন্য developer copy করতে পারবে। কিন্তু নিম্নের combination তুলনামূলকভাবে বেশি defensible হতে পারে:

1. Reliable on-device automation engine।

2. Bangladesh ও South Asia-র university timetable templates।

3. Student-friendly setup ও permission onboarding।

4. Campus community-এর মাধ্যমে distribution।

5. Privacy-first design এবং trusted automation history।

তোমার লক্ষ্য হওয়া উচিত প্রথমে হাজারো feature বানানো নয়; বরং একটি নির্দিষ্ট কাজ এত নির্ভরযোগ্যভাবে করা, যেন শিক্ষার্থীরা সেটি uninstall করতে না চায়।


# 4. সবচেয়ে গুরুত্বপূর্ণ technical reality: Android কি সত্যিই Silent করতে দেবে?

এখানে তোমার project-এর সবচেয়ে বড় engineering risk।

তুমি যেহেতু Android 15/16/17-এ চলবে এমন app বানাতে চাও, তাই Android API-কে কেন্দ্র করে architecture design করতে হবে।

## 4.1. Sound mode-এর তিনটি আলাদা জিনিস

Normal / Ring

ফোনের ringtone ও notification sound চালু থাকবে। ব্যবহারকারী নিজের volume ঠিক করবে।

Vibrate

Ringer mode Vibrate হবে, তবে vibration-এর আচরণ ফোনের system setting ও manufacturer অনুযায়ী ভিন্ন হতে পারে।

Do Not Disturb (DND)

Android-এর interruption policy ব্যবহার করে calls, notifications ও visual interruptions নিয়ন্ত্রণ করে। কোন contact, alarm বা app bypass করতে পারবে, তা user-এর system policy-এর ওপর নির্ভর করে।

### Android API-এর বাস্তব সীমাবদ্ধতা

Android 15 বা তার পরের API target করলে DND-এর global state সরাসরি নিজের মতো পরিবর্তন করার পুরোনো পদ্ধতি আর আগের মতো কাজ করে না। App-কে `AutomaticZenRule`-ভিত্তিক DND rule এবং user-granted policy access ব্যবহার করতে হয়।

![](https://www.google.com/s2/favicons?domain=https://developer.android.com\&sz=32)

Android Developers

+1

তোমার app-এর উচিত:

* User-কে Android Settings থেকে DND policy access দিতে বলা।

* নিজের app-এর নামে একটি identifiable DND rule তৈরি করা।

* প্রয়োজনীয় access না থাকলে silent automation সফল হয়েছে বলে দেখানো যাবে না।

* User-এর নিজস্ব DND rules ও exceptions সম্মান করা।

* Rule disable বা delete করার সহজ ব্যবস্থা রাখা।

Android-এর official documentation: Android 15 behavior changes ।

বিশেষ সতর্কতা: Android 17-এর নতুন background audio restrictions অনুযায়ী background অবস্থায় ringer/volume পরিবর্তনের API-তে আরও সীমাবদ্ধতা আসছে। Android 17-এ কিছু background audio interactions-এর জন্য visible activity অথবা উপযুক্ত foreground service প্রয়োজন হতে পারে; API call silently ignored হওয়ার ঝুঁকিও documented।

![](https://www.google.com/s2/favicons?domain=https://developer.android.com\&sz=32)

Android Developers

সুতরাং, আজকের ফোনে কাজ করছে বলে ভবিষ্যতের সব Android version-এ একই আচরণ করবে ধরে নেবে না।

তোমার app-এ feature-wise capability detection, OS version tests এবং device compatibility matrix বাধ্যতামূলক।

## 4.2. Native Android নাকি Flutter/React Native?

আমার প্রস্তাব:

Recommended

## Kotlin + Jetpack Compose

Android-first native application

Kotlin

Android-এর native audio, location, permissions, WorkManager ও platform APIs সরাসরি ব্যবহার করতে পারবে।

Jetpack Compose

Dashboard, settings, schedule editor, permission screens ও reusable UI components তৈরি করবে।

Room + DataStore

Timetable, automation rules, saved state, user preferences ও local event logs রাখবে।

Flutter-ও ব্যবহার করা সম্ভব, কিন্তু তোমার সবচেয়ে গুরুত্বপূর্ণ feature-গুলো native Android API-র ওপর নির্ভরশীল। Flutter ব্যবহার করলে Kotlin plugin/platform channel লিখতেই হবে। তাই প্রথম version native রাখলে debugging ও reliability testing সহজ হবে।

iOS-কে এখনই target করো না। iOS-এ third-party apps-এর silent switch, Focus modes ও system settings নিয়ন্ত্রণে অনেক বেশি সীমাবদ্ধতা আছে। Android-first strategy-তে দ্রুত একটি কার্যকর product তৈরি করা যাবে।

# 5. Complete technical architecture — প্রায় zero server cost

তোমার app-এর মূল সুবিধা হলো, এর primary functionality সম্পূর্ণ device-এ চালানো সম্ভব।

প্রস্তাবিত architecture: Local-first + Optional Cloud Sync

![Building Offline Apps: A Fullstack Approach to Mobile Resilience - Think-it](https://images.openai.com/static-rsc-4/aqHqHzaywPX6zRSMNxHSLLJJpRBMo_b7s5CMhz9qzzgfX8DhLEM_3H6XjlHE-miKFjm_A0SvX3iZL_NHxta3ntdqHFV0twVbl7D91eIdBFiFq22JjVQXp-YsZEJp1z1NxtfAs_cAFLTVDW5LL6zvWsDhlkLw5iHaaACiwFFNB0A?purpose=inline)

## 5.1. System architecture

Diagram options

![](data\:image/svg+xml;utf8,%3Csvg%20id%3D%22mermaid-_r_16p_%22%20width%3D%22830.623779296875%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20class%3D%22flowchart%22%20height%3D%221136.638916015625%22%20viewBox%3D%224%203.9999961853027344%20830.623779296875%201136.638916015625%22%20role%3D%22graphics-document%20document%22%20aria-roledescription%3D%22flowchart-v2%22%3E%3Cstyle%3E%23mermaid-_r_16p_%7Bfont-family%3A%22-apple-system%22%2C%22BlinkMacSystemFont%22%2C%22Segoe%20UI%22%2C%22Roboto%22%2C%22Oxygen%22%2C%22Ubuntu%22%2C%22Cantarell%22%2C%22Helvetica%20Neue%22%2C%22Arial%22%2C%22sans-serif%22%3Bfont-size%3A14px%3Bfill%3Argb\(255%2C%20255%2C%20255\)%3B%7D%40keyframes%20edge-animation-frame%7Bfrom%7Bstroke-dashoffset%3A0%3B%7D%7D%40keyframes%20dash%7Bto%7Bstroke-dashoffset%3A0%3B%7D%7D%23mermaid-_r_16p_%20.edge-animation-slow%7Bstroke-dasharray%3A9%2C5!important%3Bstroke-dashoffset%3A900%3Banimation%3Adash%2050s%20linear%20infinite%3Bstroke-linecap%3Around%3B%7D%23mermaid-_r_16p_%20.edge-animation-fast%7Bstroke-dasharray%3A9%2C5!important%3Bstroke-dashoffset%3A900%3Banimation%3Adash%2020s%20linear%20infinite%3Bstroke-linecap%3Around%3B%7D%23mermaid-_r_16p_%20.error-icon%7Bfill%3Argb\(33%2C%2033%2C%2033\)%3B%7D%23mermaid-_r_16p_%20.error-text%7Bfill%3Argb\(255%2C%20255%2C%20255\)%3Bstroke%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.edge-thickness-normal%7Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.edge-thickness-thick%7Bstroke-width%3A3.5px%3B%7D%23mermaid-_r_16p_%20.edge-pattern-solid%7Bstroke-dasharray%3A0%3B%7D%23mermaid-_r_16p_%20.edge-thickness-invisible%7Bstroke-width%3A0%3Bfill%3Anone%3B%7D%23mermaid-_r_16p_%20.edge-pattern-dashed%7Bstroke-dasharray%3A3%3B%7D%23mermaid-_r_16p_%20.edge-pattern-dotted%7Bstroke-dasharray%3A2%3B%7D%23mermaid-_r_16p_%20.marker%7Bfill%3Argb\(205%2C%20205%2C%20205\)%3Bstroke%3Argb\(205%2C%20205%2C%20205\)%3B%7D%23mermaid-_r_16p_%20.marker.cross%7Bstroke%3Argb\(205%2C%20205%2C%20205\)%3B%7D%23mermaid-_r_16p_%20svg%7Bfont-family%3A%22-apple-system%22%2C%22BlinkMacSystemFont%22%2C%22Segoe%20UI%22%2C%22Roboto%22%2C%22Oxygen%22%2C%22Ubuntu%22%2C%22Cantarell%22%2C%22Helvetica%20Neue%22%2C%22Arial%22%2C%22sans-serif%22%3Bfont-size%3A14px%3B%7D%23mermaid-_r_16p_%20p%7Bmargin%3A0%3B%7D%23mermaid-_r_16p_%20.label%7Bfont-family%3A%22-apple-system%22%2C%22BlinkMacSystemFont%22%2C%22Segoe%20UI%22%2C%22Roboto%22%2C%22Oxygen%22%2C%22Ubuntu%22%2C%22Cantarell%22%2C%22Helvetica%20Neue%22%2C%22Arial%22%2C%22sans-serif%22%3Bcolor%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.cluster-label%20text%7Bfill%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.cluster-label%20span%7Bcolor%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.cluster-label%20span%20p%7Bbackground-color%3Atransparent%3B%7D%23mermaid-_r_16p_%20.label%20text%2C%23mermaid-_r_16p_%20span%7Bfill%3Argb\(255%2C%20255%2C%20255\)%3Bcolor%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.node%20rect%2C%23mermaid-_r_16p_%20.node%20circle%2C%23mermaid-_r_16p_%20.node%20ellipse%2C%23mermaid-_r_16p_%20.node%20polygon%2C%23mermaid-_r_16p_%20.node%20path%7Bfill%3Argb\(51%2C%2024%2C%2037\)%3Bstroke%3Argb\(171%2C%2079%2C%20122\)%3Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.rough-node%20.label%20text%2C%23mermaid-_r_16p_%20.node%20.label%20text%2C%23mermaid-_r_16p_%20.image-shape%20.label%2C%23mermaid-_r_16p_%20.icon-shape%20.label%7Btext-anchor%3Amiddle%3B%7D%23mermaid-_r_16p_%20.node%20.katex%20path%7Bfill%3A%23000%3Bstroke%3A%23000%3Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.rough-node%20.label%2C%23mermaid-_r_16p_%20.node%20.label%2C%23mermaid-_r_16p_%20.image-shape%20.label%2C%23mermaid-_r_16p_%20.icon-shape%20.label%7Btext-align%3Acenter%3B%7D%23mermaid-_r_16p_%20.node.clickable%7Bcursor%3Apointer%3B%7D%23mermaid-_r_16p_%20.root%20.anchor%20path%7Bfill%3Argb\(205%2C%20205%2C%20205\)!important%3Bstroke-width%3A0%3Bstroke%3Argb\(205%2C%20205%2C%20205\)%3B%7D%23mermaid-_r_16p_%20.arrowheadPath%7Bfill%3Argb\(205%2C%20205%2C%20205\)%3B%7D%23mermaid-_r_16p_%20.edgePath%20.path%7Bstroke%3Argb\(205%2C%20205%2C%20205\)%3Bstroke-width%3A2.0px%3B%7D%23mermaid-_r_16p_%20.flowchart-link%7Bstroke%3Argb\(205%2C%20205%2C%20205\)%3Bfill%3Anone%3B%7D%23mermaid-_r_16p_%20.edgeLabel%7Bbackground-color%3Argb\(0%2C%200%2C%200\)%3Btext-align%3Acenter%3B%7D%23mermaid-_r_16p_%20.edgeLabel%20p%7Bbackground-color%3Argb\(0%2C%200%2C%200\)%3B%7D%23mermaid-_r_16p_%20.edgeLabel%20rect%7Bopacity%3A0.5%3Bbackground-color%3Argb\(0%2C%200%2C%200\)%3Bfill%3Argb\(0%2C%200%2C%200\)%3B%7D%23mermaid-_r_16p_%20.labelBkg%7Bbackground-color%3Argba\(0%2C%200%2C%200%2C%200.5\)%3B%7D%23mermaid-_r_16p_%20.cluster%20rect%7Bfill%3Argb\(33%2C%2033%2C%2033\)%3Bstroke%3Argba\(255%2C%20255%2C%20255%2C%200.05\)%3Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.cluster%20text%7Bfill%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20.cluster%20span%7Bcolor%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20div.mermaidTooltip%7Bposition%3Aabsolute%3Btext-align%3Acenter%3Bmax-width%3A200px%3Bpadding%3A2px%3Bfont-family%3A%22-apple-system%22%2C%22BlinkMacSystemFont%22%2C%22Segoe%20UI%22%2C%22Roboto%22%2C%22Oxygen%22%2C%22Ubuntu%22%2C%22Cantarell%22%2C%22Helvetica%20Neue%22%2C%22Arial%22%2C%22sans-serif%22%3Bfont-size%3A12px%3Bbackground%3Argb\(33%2C%2033%2C%2033\)%3Bborder%3A1px%20solid%20rgba\(255%2C%20255%2C%20255%2C%200.05\)%3Bborder-radius%3A2px%3Bpointer-events%3Anone%3Bz-index%3A100%3B%7D%23mermaid-_r_16p_%20.flowchartTitleText%7Btext-anchor%3Amiddle%3Bfont-size%3A18px%3Bfill%3Argb\(255%2C%20255%2C%20255\)%3B%7D%23mermaid-_r_16p_%20rect.text%7Bfill%3Anone%3Bstroke-width%3A0%3B%7D%23mermaid-_r_16p_%20.icon-shape%2C%23mermaid-_r_16p_%20.image-shape%7Bbackground-color%3Argb\(0%2C%200%2C%200\)%3Btext-align%3Acenter%3B%7D%23mermaid-_r_16p_%20.icon-shape%20p%2C%23mermaid-_r_16p_%20.image-shape%20p%7Bbackground-color%3Argb\(0%2C%200%2C%200\)%3Bpadding%3A2px%3B%7D%23mermaid-_r_16p_%20.icon-shape%20rect%2C%23mermaid-_r_16p_%20.image-shape%20rect%7Bopacity%3A0.5%3Bbackground-color%3Argb\(0%2C%200%2C%200\)%3Bfill%3Argb\(0%2C%200%2C%200\)%3B%7D%23mermaid-_r_16p_%20.label-icon%7Bdisplay%3Ainline-block%3Bheight%3A1em%3Boverflow%3Avisible%3Bvertical-align%3A-0.125em%3B%7D%23mermaid-_r_16p_%20.node%20.label-icon%20path%7Bfill%3AcurrentColor%3Bstroke%3Arevert%3Bstroke-width%3Arevert%3B%7D%23mermaid-_r_16p_%20.node%20text%7Bfont-size%3A16px%3Bfont-weight%3A600%3Bletter-spacing%3A-0.32px%3Bfill%3A%23ffbada%3B%7D%23mermaid-_r_16p_%20.edgeLabels%20text%7Bfont-size%3A13px%3Bfont-weight%3A600%3Bletter-spacing%3A-0.08px%3Bfill%3A%23ffbada%3B%7D%23mermaid-_r_16p_%20.node%20tspan%5Bfont-weight%3D%22normal%22%5D%2C%23mermaid-_r_16p_%20.edgeLabels%20tspan%5Bfont-weight%3D%22normal%22%5D%7Bfont-weight%3A600%3B%7D%23mermaid-_r_16p_%20.edgeLabel%20.label%20rect%7Bopacity%3A1%3Brx%3A13px%3Bry%3A13px%3Bfill%3A%2329101c%3Bstroke%3Argb\(95%2C%2053%2C%2072\)%3Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.node%20rect%2C%23mermaid-_r_16p_%20.node%20circle%2C%23mermaid-_r_16p_%20.node%20ellipse%2C%23mermaid-_r_16p_%20.node%20polygon%2C%23mermaid-_r_16p_%20.node%20path%7Bfill%3Argb\(77%2C%2031%2C%2052\)%3Bstroke%3Argba\(255%2C%20255%2C%20255%2C%200.1\)%3Bstroke-width%3A1px%3B%7D%23mermaid-_r_16p_%20.node%20rect%7Brx%3A16px%3Bry%3A16px%3B%7D%23mermaid-_r_16p_%20.node.mermaid-decision%20.label-container%7Bfill%3A%2329101c%3Bstroke%3Argb\(95%2C%2053%2C%2072\)%3Bstroke-dasharray%3A2%202%3B%7D%23mermaid-_r_16p_%20.edgePaths%20.flowchart-link%7Bstroke%3Argb\(95%2C%2053%2C%2072\)%3Bstroke-width%3A1px%3Bstroke-linecap%3Around%3Bstroke-linejoin%3Around%3B%7D%23mermaid-_r_16p_%20.marker%7Bfill%3Argb\(95%2C%2053%2C%2072\)%3Bstroke%3Argb\(95%2C%2053%2C%2072\)%3B%7D%23mermaid-_r_16p_%20.node%7Bcolor-scheme%3Adark%3B%7D%23mermaid-_r_16p_%20%3Aroot%7B--mermaid-font-family%3A%22-apple-system%22%2C%22BlinkMacSystemFont%22%2C%22Segoe%20UI%22%2C%22Roboto%22%2C%22Oxygen%22%2C%22Ubuntu%22%2C%22Cantarell%22%2C%22Helvetica%20Neue%22%2C%22Arial%22%2C%22sans-serif%22%3B%7D%3C%2Fstyle%3E%3Cg%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-pointEnd%22%20class%3D%22marker%20flowchart-v2%22%20viewBox%3D%22-5%20-5%2010%2010%22%20refX%3D%220%22%20refY%3D%220%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2210%22%20markerHeight%3D%2210%22%20orient%3D%22auto%22%3E%3Cpath%20d%3D%22M%200%200%20L%204%200%20M%200.8180194846605362%20-3.181980515339464%20L%204%200%20L%200.8180194846605362%203.181980515339464%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%201%3B%20stroke-dasharray%3A%20none%3B%20fill%3A%20none%3B%20stroke-linecap%3A%20round%3B%20stroke-linejoin%3A%20round%3B%22%3E%3C%2Fpath%3E%3C%2Fmarker%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-pointStart%22%20class%3D%22marker%20flowchart-v2%22%20viewBox%3D%22-5%20-5%2010%2010%22%20refX%3D%220%22%20refY%3D%220%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2210%22%20markerHeight%3D%2210%22%20orient%3D%22auto%22%3E%3Cpath%20d%3D%22M%200%200%20L%20-4%200%20M%20-0.8180194846605362%20-3.181980515339464%20L%20-4%200%20L%20-0.8180194846605362%203.181980515339464%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%201%3B%20stroke-dasharray%3A%20none%3B%20fill%3A%20none%3B%20stroke-linecap%3A%20round%3B%20stroke-linejoin%3A%20round%3B%22%3E%3C%2Fpath%3E%3C%2Fmarker%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-circleEnd%22%20class%3D%22marker%20flowchart-v2%22%20viewBox%3D%220%200%2010%2010%22%20refX%3D%2211%22%20refY%3D%225%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2211%22%20markerHeight%3D%2211%22%20orient%3D%22auto%22%3E%3Ccircle%20cx%3D%225%22%20cy%3D%225%22%20r%3D%225%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%201%3B%20stroke-dasharray%3A%201%2C%200%3B%22%3E%3C%2Fcircle%3E%3C%2Fmarker%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-circleStart%22%20class%3D%22marker%20flowchart-v2%22%20viewBox%3D%220%200%2010%2010%22%20refX%3D%22-1%22%20refY%3D%225%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2211%22%20markerHeight%3D%2211%22%20orient%3D%22auto%22%3E%3Ccircle%20cx%3D%225%22%20cy%3D%225%22%20r%3D%225%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%201%3B%20stroke-dasharray%3A%201%2C%200%3B%22%3E%3C%2Fcircle%3E%3C%2Fmarker%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-crossEnd%22%20class%3D%22marker%20cross%20flowchart-v2%22%20viewBox%3D%220%200%2011%2011%22%20refX%3D%2212%22%20refY%3D%225.2%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2211%22%20markerHeight%3D%2211%22%20orient%3D%22auto%22%3E%3Cpath%20d%3D%22M%201%2C1%20l%209%2C9%20M%2010%2C1%20l%20-9%2C9%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%202%3B%20stroke-dasharray%3A%201%2C%200%3B%22%3E%3C%2Fpath%3E%3C%2Fmarker%3E%3Cmarker%20id%3D%22mermaid-_r_16p__flowchart-v2-crossStart%22%20class%3D%22marker%20cross%20flowchart-v2%22%20viewBox%3D%220%200%2011%2011%22%20refX%3D%22-1%22%20refY%3D%225.2%22%20markerUnits%3D%22userSpaceOnUse%22%20markerWidth%3D%2211%22%20markerHeight%3D%2211%22%20orient%3D%22auto%22%3E%3Cpath%20d%3D%22M%201%2C1%20l%209%2C9%20M%2010%2C1%20l%20-9%2C9%22%20class%3D%22arrowMarkerPath%22%20style%3D%22stroke-width%3A%202%3B%20stroke-dasharray%3A%201%2C%200%3B%22%3E%3C%2Fpath%3E%3C%2Fmarker%3E%3C%2Fg%3E%3Cg%20class%3D%22subgraphs%22%3E%3C%2Fg%3E%3Cg%20class%3D%22nodes%22%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-A-0%22%20transform%3D%22translate\(651.1857833862305%2C%2042\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-132.17471313476562%22%20y%3D%22-30%22%20width%3D%22264.34942626953125%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EUser%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Setup%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20and%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Permissions%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-B-1%22%20transform%3D%22translate\(651.1857833862305%2C%20142\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-107.13909912109375%22%20y%3D%22-30%22%20width%3D%22214.2781982421875%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EJetpack%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Compose%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20UI%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-C-3%22%20transform%3D%22translate\(651.1857833862305%2C%20242\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-106.78177642822266%22%20y%3D%22-30%22%20width%3D%22213.5635528564453%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EDomain%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Rule%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Engine%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-D-5%22%20transform%3D%22translate\(686.4518610636393%2C%20802.6388549804688\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-91.21814727783203%22%20y%3D%22-30%22%20width%3D%22182.43629455566406%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ERoom%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Database%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-E-7%22%20transform%3D%22translate\(449.5912437438965%2C%20367.1597137451172\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-120.53838348388672%22%20y%3D%22-30%22%20width%3D%22241.07676696777344%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EAndroid%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Geofencing%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20API%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-F-9%22%20transform%3D%22translate\(704.5766716003418%2C%20367.1597137451172\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-94.4470443725586%22%20y%3D%22-35.15971374511719%22%20width%3D%22188.8940887451172%22%20height%3D%2270.31942749023438%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-19.159713745117188\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EAlarmManager%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20%2F%3C%2Ftspan%3E%3C%2Ftspan%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%221em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EWorkManager%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-G-11%22%20transform%3D%22translate\(661.032283782959%2C%20472.3194274902344\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-130.6331558227539%22%20y%3D%22-30%22%20width%3D%22261.2663116455078%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EAutomation%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Event%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Receiver%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-H-15%22%20transform%3D%22translate\(661.032283782959%2C%20572.3194274902344\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-111.65125274658203%22%20y%3D%22-30%22%20width%3D%22223.30250549316406%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ERule%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Conflict%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Resolver%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-I-17%22%20transform%3D%22translate\(152.2271270751953%2C%20692.3194274902344\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-119.22713470458984%22%20y%3D%22-30%22%20width%3D%22238.4542694091797%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EAndroid%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20AudioManager%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-J-19%22%20transform%3D%22translate\(439.27315521240234%2C%20697.4791412353516\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-127.81890106201172%22%20y%3D%22-35.15971374511719%22%20width%3D%22255.63780212402344%22%20height%3D%2270.31942749023438%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-19.159713745117188\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ENotificationManager%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20DND%3C%2Ftspan%3E%3C%2Ftspan%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%221em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ERule%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-K-21%22%20transform%3D%22translate\(716.85791015625%2C%20697.4791412353516\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-109.76585388183594%22%20y%3D%22-35.15971374511719%22%20width%3D%22219.53170776367188%22%20height%3D%2270.31942749023438%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-19.159713745117188\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ESaved%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20State%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20%2F%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Restore%3C%2Ftspan%3E%3C%2Ftspan%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%221em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EManager%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-L-25%22%20transform%3D%22translate\(509.4566192626953%2C%20902.6388549804688\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-129.90128326416016%22%20y%3D%22-30%22%20width%3D%22259.8025665283203%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ELocal%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Analytics%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20and%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20History%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-M-27%22%20transform%3D%22translate\(716.85791015625%2C%201002.6388549804688\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-74.80294418334961%22%20y%3D%22-30%22%20width%3D%22149.60588836669922%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ECloud%3C%2Ftspan%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3E%20Sync%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22node%20default%22%20id%3D%22flowchart-N-29%22%20transform%3D%22translate\(716.85791015625%2C%201102.6388549804688\)%22%3E%3Crect%20class%3D%22basic%20label-container%22%20style%3D%22%22%20x%3D%22-69.36344909667969%22%20y%3D%22-30%22%20width%3D%22138.72689819335938%22%20height%3D%2260%22%3E%3C%2Frect%3E%3Cg%20class%3D%22label%22%20style%3D%22%22%20transform%3D%22translate\(0%2C%20-10.359713554382324\)%22%3E%3Crect%3E%3C%2Frect%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3ESupabase%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edges%20edgePaths%22%3E%3Cpath%20d%3D%22M651.1857833862305%2C72L651.1857833862305%2C100%22%20id%3D%22L_A_B_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_A_B_0%22%20data-points%3D%22W3sieCI6NjUxLjE4NTc4MzM4NjIzMDUsInkiOjcyfSx7IngiOjY1MS4xODU3ODMzODYyMzA1LCJ5IjoxMDR9XQ%3D%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M651.1857833862305%2C172L651.1857833862305%2C200%22%20id%3D%22L_B_C_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_B_C_0%22%20data-points%3D%22W3sieCI6NjUxLjE4NTc4MzM4NjIzMDUsInkiOjE3Mn0seyJ4Ijo2NTEuMTg1NzgzMzg2MjMwNSwieSI6MjA0fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M597.7948951721191%2C272L597.7948951721191%2C285.21704397470535Q597.7948951721191%2C287%20596.7091087344922%2C288.4142135623731L596.7091087344922%2C288.4142135623731Q595.6233222968654%2C289.8284271247462%20594.2091087344922%2C290.9142135623731L594.2091087344922%2C290.9142135623731Q592.7948951721191%2C292%20591.0119391468245%2C292L18.782956025294652%2C292Q17%2C292%2015.585786437626904%2C293.0857864376269L15.585786437626904%2C293.0857864376269Q14.17157287525381%2C294.1715728752538%2013.085786437626915%2C295.5857864376269L13.085786437626904%2C295.5857864376269Q12%2C297%2012%2C298.78295602529465L12%2C367.1597137451172L12%2C472.3194274902344L12%2C572.3194274902344L12%2C697.4791412353516L12%2C745.8558989551741Q12%2C747.6388549804688%2013.085786437626904%2C749.0530685428419L13.085786437626915%2C749.0530685428419Q14.17157287525381%2C750.467282105215%2015.585786437626904%2C751.5530685428419L15.585786437626904%2C751.5530685428419Q17%2C752.6388549804688%2018.782956025294652%2C752.6388549804688L649.262855945734%2C752.6388549804688Q651.0458119710287%2C752.6388549804688%20652.4600255334018%2C753.7246414180956L652.4600255334018%2C753.7246414180956Q653.8742390957749%2C754.8104278557225%20654.9600255334018%2C756.2246414180956L654.9600255334018%2C756.2246414180956Q656.0458119710287%2C757.6388549804688%20656.0458119710287%2C759.4218110057634L656.0458119710287%2C762.6388549804688%22%20id%3D%22L_C_D_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_C_D_0%22%20data-points%3D%22W3sieCI6NTk3Ljc5NDg5NTE3MjExOTEsInkiOjI3Mn0seyJ4Ijo1OTcuNzk0ODk1MTcyMTE5MSwieSI6MjkyfSx7IngiOjEyLCJ5IjoyOTJ9LHsieCI6MTIsInkiOjM2Ny4xNTk3MTM3NDUxMTcyfSx7IngiOjEyLCJ5Ijo0NzIuMzE5NDI3NDkwMjM0NH0seyJ4IjoxMiwieSI6NTcyLjMxOTQyNzQ5MDIzNDR9LHsieCI6MTIsInkiOjY5Ny40NzkxNDEyMzUzNTE2fSx7IngiOjEyLCJ5Ijo3NTIuNjM4ODU0OTgwNDY4OH0seyJ4Ijo2NTYuMDQ1ODExOTcxMDI4NywieSI6NzUyLjYzODg1NDk4MDQ2ODh9LHsieCI6NjU2LjA0NTgxMTk3MTAyODcsInkiOjc2Ni42Mzg4NTQ5ODA0Njg4fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M651.1857833862305%2C272L651.1857833862305%2C305.21704397470535Q651.1857833862305%2C307%20650.0999969486036%2C308.4142135623731L650.0999969486036%2C308.4142135623731Q649.0142105109767%2C309.8284271247462%20647.5999969486036%2C310.9142135623731L647.5999969486036%2C310.9142135623731Q646.1857833862305%2C312%20644.4028273609358%2C312L456.37419976919114%2C312Q454.5912437438965%2C312%20453.1770301815234%2C313.0857864376269L453.1770301815234%2C313.0857864376269Q451.76281661915027%2C314.1715728752538%20450.6770301815234%2C315.5857864376269L450.6770301815234%2C315.5857864376269Q449.5912437438965%2C317%20449.5912437438965%2C318.78295602529465L449.5912437438965%2C325.1597137451172%22%20id%3D%22L_C_E_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_C_E_0%22%20data-points%3D%22W3sieCI6NjUxLjE4NTc4MzM4NjIzMDUsInkiOjI3Mn0seyJ4Ijo2NTEuMTg1NzgzMzg2MjMwNSwieSI6MzEyfSx7IngiOjQ0OS41OTEyNDM3NDM4OTY1LCJ5IjozMTJ9LHsieCI6NDQ5LjU5MTI0Mzc0Mzg5NjUsInkiOjMyOS4xNTk3MTM3NDUxMTcyfV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M704.5766716003418%2C272L704.5766716003418%2C320%22%20id%3D%22L_C_F_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_C_F_0%22%20data-points%3D%22W3sieCI6NzA0LjU3NjY3MTYwMDM0MTgsInkiOjI3Mn0seyJ4Ijo3MDQuNTc2NjcxNjAwMzQxOCwieSI6MzI0fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M449.5912437438965%2C397.1597137451172L449.5912437438965%2C415.5364714649397Q449.5912437438965%2C417.3194274902344%20450.6770301815234%2C418.7336410526075L450.6770301815234%2C418.7336410526075Q451.76281661915027%2C420.1478546149806%20453.1770301815234%2C421.2336410526075L453.1770301815234%2C421.2336410526075Q454.5912437438965%2C422.3194274902344%20456.37419976919114%2C422.3194274902344L610.7049399402815%2C422.3194274902344Q612.4878959655762%2C422.3194274902344%20613.9021095279493%2C423.40521392786127L613.9021095279493%2C423.40521392786127Q615.3163230903224%2C424.49100036548816%20616.4021095279493%2C425.90521392786127L616.4021095279493%2C425.90521392786127Q617.4878959655762%2C427.3194274902344%20617.4878959655762%2C429.102383515529L617.4878959655762%2C432.3194274902344%22%20id%3D%22L_E_G_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_E_G_0%22%20data-points%3D%22W3sieCI6NDQ5LjU5MTI0Mzc0Mzg5NjUsInkiOjM5Ny4xNTk3MTM3NDUxMTcyfSx7IngiOjQ0OS41OTEyNDM3NDM4OTY1LCJ5Ijo0MjIuMzE5NDI3NDkwMjM0NH0seyJ4Ijo2MTcuNDg3ODk1OTY1NTc2MiwieSI6NDIyLjMxOTQyNzQ5MDIzNDR9LHsieCI6NjE3LjQ4Nzg5NTk2NTU3NjIsInkiOjQzNi4zMTk0Mjc0OTAyMzQ0fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M704.5766716003418%2C402.3194274902344L704.5766716003418%2C430.3194274902344%22%20id%3D%22L_F_G_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_F_G_0%22%20data-points%3D%22W3sieCI6NzA0LjU3NjY3MTYwMDM0MTgsInkiOjQwMi4zMTk0Mjc0OTAyMzQ0fSx7IngiOjcwNC41NzY2NzE2MDAzNDE4LCJ5Ijo0MzQuMzE5NDI3NDkwMjM0NH1d%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M661.032283782959%2C502.3194274902344L661.032283782959%2C530.3194274902344%22%20id%3D%22L_G_H_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_G_H_0%22%20data-points%3D%22W3sieCI6NjYxLjAzMjI4Mzc4Mjk1OSwieSI6NTAyLjMxOTQyNzQ5MDIzNDR9LHsieCI6NjYxLjAzMjI4Mzc4Mjk1OSwieSI6NTM0LjMxOTQyNzQ5MDIzNDR9XQ%3D%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M605.206657409668%2C602.3194274902344L605.206657409668%2C615.5364714649397Q605.206657409668%2C617.3194274902344%20604.1208709720411%2C618.7336410526075L604.1208709720411%2C618.7336410526075Q603.0350845344142%2C620.1478546149806%20601.6208709720411%2C621.2336410526075L601.6208709720411%2C621.2336410526075Q600.206657409668%2C622.3194274902344%20598.4237013843733%2C622.3194274902344L159.01008310048996%2C622.3194274902344Q157.2271270751953%2C622.3194274902344%20155.8129135128222%2C623.4052139278613L155.8129135128222%2C623.4052139278613Q154.39869995044913%2C624.4910003654882%20153.31291351282223%2C625.9052139278613L153.3129135128222%2C625.9052139278613Q152.2271270751953%2C627.3194274902344%20152.2271270751953%2C629.102383515529L152.2271270751953%2C650.3194274902344%22%20id%3D%22L_H_I_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_H_I_0%22%20data-points%3D%22W3sieCI6NjA1LjIwNjY1NzQwOTY2OCwieSI6NjAyLjMxOTQyNzQ5MDIzNDR9LHsieCI6NjA1LjIwNjY1NzQwOTY2OCwieSI6NjIyLjMxOTQyNzQ5MDIzNDR9LHsieCI6MTUyLjIyNzEyNzA3NTE5NTMsInkiOjYyMi4zMTk0Mjc0OTAyMzQ0fSx7IngiOjE1Mi4yMjcxMjcwNzUxOTUzLCJ5Ijo2NTQuMzE5NDI3NDkwMjM0NH1d%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M661.032283782959%2C602.3194274902344L661.032283782959%2C635.5364714649397Q661.032283782959%2C637.3194274902344%20659.9464973453321%2C638.7336410526075L659.9464973453321%2C638.7336410526075Q658.8607109077052%2C640.1478546149806%20657.4464973453321%2C641.2336410526075L657.4464973453321%2C641.2336410526075Q656.032283782959%2C642.3194274902344%20654.2493277576643%2C642.3194274902344L446.056111237697%2C642.3194274902344Q444.27315521240234%2C642.3194274902344%20442.85894165002924%2C643.4052139278613L442.85894165002924%2C643.4052139278613Q441.44472808765613%2C644.4910003654882%20440.35894165002924%2C645.9052139278613L440.35894165002924%2C645.9052139278613Q439.27315521240234%2C647.3194274902344%20439.27315521240234%2C649.102383515529L439.27315521240234%2C652.3194274902344%22%20id%3D%22L_H_J_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_H_J_0%22%20data-points%3D%22W3sieCI6NjYxLjAzMjI4Mzc4Mjk1OSwieSI6NjAyLjMxOTQyNzQ5MDIzNDR9LHsieCI6NjYxLjAzMjI4Mzc4Mjk1OSwieSI6NjQyLjMxOTQyNzQ5MDIzNDR9LHsieCI6NDM5LjI3MzE1NTIxMjQwMjM0LCJ5Ijo2NDIuMzE5NDI3NDkwMjM0NH0seyJ4Ijo0MzkuMjczMTU1MjEyNDAyMzQsInkiOjY1Ni4zMTk0Mjc0OTAyMzQ0fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M716.85791015625%2C602.3194274902344L716.85791015625%2C650.3194274902344%22%20id%3D%22L_H_K_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_H_K_0%22%20data-points%3D%22W3sieCI6NzE2Ljg1NzkxMDE1NjI1LCJ5Ijo2MDIuMzE5NDI3NDkwMjM0NH0seyJ4Ijo3MTYuODU3OTEwMTU2MjUsInkiOjY1NC4zMTk0Mjc0OTAyMzQ0fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M716.85791015625%2C732.6388549804688L716.85791015625%2C760.6388549804688%22%20id%3D%22L_K_D_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_K_D_0%22%20data-points%3D%22W3sieCI6NzE2Ljg1NzkxMDE1NjI1LCJ5Ijo3MzIuNjM4ODU0OTgwNDY4OH0seyJ4Ijo3MTYuODU3OTEwMTU2MjUsInkiOjc2NC42Mzg4NTQ5ODA0Njg4fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M656.0458119710287%2C832.6388549804688L656.0458119710287%2C845.8558989551741Q656.0458119710287%2C847.6388549804688%20654.9600255334018%2C849.0530685428419L654.9600255334018%2C849.0530685428419Q653.8742390957749%2C850.467282105215%20652.4600255334018%2C851.5530685428419L652.4600255334018%2C851.5530685428419Q651.0458119710287%2C852.6388549804688%20649.262855945734%2C852.6388549804688L516.23957528799%2C852.6388549804688Q514.4566192626953%2C852.6388549804688%20513.0424057003222%2C853.7246414180956L513.0424057003222%2C853.7246414180956Q511.6281921379491%2C854.8104278557225%20510.5424057003222%2C856.2246414180956L510.5424057003222%2C856.2246414180956Q509.4566192626953%2C857.6388549804688%20509.4566192626953%2C859.4218110057634L509.4566192626953%2C862.6388549804688%22%20id%3D%22L_D_L_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-solid%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_D_L_0%22%20data-points%3D%22W3sieCI6NjU2LjA0NTgxMTk3MTAyODcsInkiOjgzMi42Mzg4NTQ5ODA0Njg4fSx7IngiOjY1Ni4wNDU4MTE5NzEwMjg3LCJ5Ijo4NTIuNjM4ODU0OTgwNDY4OH0seyJ4Ijo1MDkuNDU2NjE5MjYyNjk1MywieSI6ODUyLjYzODg1NDk4MDQ2ODh9LHsieCI6NTA5LjQ1NjYxOTI2MjY5NTMsInkiOjg2Ni42Mzg4NTQ5ODA0Njg4fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M716.85791015625%2C832.6388549804688L716.85791015625%2C960.6388549804688%22%20id%3D%22L_D_M_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-dotted%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_D_M_0%22%20data-points%3D%22W3sieCI6NzE2Ljg1NzkxMDE1NjI1LCJ5Ijo4MzIuNjM4ODU0OTgwNDY4OH0seyJ4Ijo3MTYuODU3OTEwMTU2MjUsInkiOjk2NC42Mzg4NTQ5ODA0Njg4fV0%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3Cpath%20d%3D%22M716.85791015625%2C1032.6388549804688L716.85791015625%2C1060.6388549804688%22%20id%3D%22L_M_N_0%22%20class%3D%22edge-thickness-normal%20edge-pattern-dotted%20edge-thickness-normal%20edge-pattern-solid%20flowchart-link%22%20style%3D%22%3B%22%20data-edge%3D%22true%22%20data-et%3D%22edge%22%20data-id%3D%22L_M_N_0%22%20data-points%3D%22W3sieCI6NzE2Ljg1NzkxMDE1NjI1LCJ5IjoxMDMyLjYzODg1NDk4MDQ2ODh9LHsieCI6NzE2Ljg1NzkxMDE1NjI1LCJ5IjoxMDY0LjYzODg1NDk4MDQ2ODh9XQ%3D%3D%22%20marker-end%3D%22url\(%23mermaid-_r_16p__flowchart-v2-pointEnd\)%22%3E%3C%2Fpath%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabels%22%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22stroke%3A%20none%22%3E%3C%2Frect%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_A_B_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_B_C_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_C_D_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_C_E_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_C_F_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_E_G_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_F_G_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_G_H_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_H_I_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_H_J_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_H_K_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_K_D_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_D_L_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%20transform%3D%22translate\(716.6816558837891%2C%20902.6388549804688\)%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_D_M_0%22%20transform%3D%22translate\(-25.323745727539062%2C-7.6282958984375\)%22%3E%3Cg%3E%3Crect%20class%3D%22background%22%20style%3D%22%22%20x%3D%22-12%22%20y%3D%22-5.371703863143921%22%20width%3D%2274.64748764038086%22%20height%3D%2226%22%3E%3C%2Frect%3E%3Ctext%20y%3D%22-10.1%22%20style%3D%22%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3Ctspan%20font-style%3D%22normal%22%20class%3D%22text-inner-tspan%22%20font-weight%3D%22normal%22%3EOptional%3C%2Ftspan%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3Cg%20class%3D%22edgeLabel%22%3E%3Cg%20class%3D%22label%22%20data-id%3D%22L_M_N_0%22%20transform%3D%22translate\(0%2C%200\)%22%3E%3Ctext%20y%3D%22-10.1%22%3E%3Ctspan%20class%3D%22text-outer-tspan%22%20x%3D%220%22%20y%3D%22-0.1em%22%20dy%3D%221.1em%22%3E%3C%2Ftspan%3E%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E)

এখানে মূল automation flow-তে Supabase, Node.js server বা কোনো AI API নেই।

ফোনের ভেতরেই timetable পড়বে, location event গ্রহণ করবে, active rules resolve করবে এবং Android system API ব্যবহার করে sound profile apply করবে।

## 5.2. Recommended technology stack

|
Layer

|

Technology

|

Server cost

|
| --- | --- | --- |
|

Mobile UI

|

Kotlin + Jetpack Compose

|

$0

|
|

Local DB

|

Room (SQLite)

|

$0

|
|

Preferences

|

DataStore

|

$0

|
|

Time triggers

|

AlarmManager + WorkManager

|

$0

|
|

Geofencing

|

Google Play Services Geofencing API

|

সাধারণ ব্যবহারে app server লাগে না

|
|

Location selection

|

Maps/Places SDK অথবা map picker

|

Usage ও billing অনুযায়ী

|
|

Background events

|

BroadcastReceiver, Boot receiver, প্রয়োজনমতো foreground service

|

$0 server

|
|

Optional auth

|

Supabase Auth

|

Free tier দিয়ে শুরু

|
|

Optional cloud DB

|

Supabase PostgreSQL

|

Free tier দিয়ে শুরু

|
|

Optional push

|

Firebase Cloud Messaging

|

সাধারণ push delivery-তে আলাদা server প্রয়োজন নেই

|

Official documentation: Android geofencing  এবং Android WorkManager ।

### Location tracking-এর সঠিক পদ্ধতি

প্রতি ৫ সেকেন্ডে GPS poll করবে না। এতে battery drain হবে এবং unnecessary background activity তৈরি হবে।

তার বদলে:

1. User একটি geofence তৈরি করবে।

2. Android geofencing service enter/exit events দেবে।

3. App event পাওয়ার পর location condition ও schedule যাচাই করবে।

4. প্রয়োজনীয় sound mode apply করবে।

5. Exit বা schedule end হলে restore logic চালাবে।

Geofencing-এ event delivery delayed হতে পারে। GPS, Wi-Fi, connectivity, battery optimization ও manufacturer-specific restrictions-এর কারণে exact boundary-তে সবসময় instant event পাওয়া যাবে না।

তাই ১৫০–৩০০ মিটার radius দিয়ে শুরু করে real-device testing করবে। Classroom-level ১০ মিটার GPS accuracy দাবি করবে না। Indoor classroom-এ GPS নির্ভরযোগ্য নাও হতে পারে।

## 5.3. Time-based automation কীভাবে বানাবে?

দুটি কাজ আলাদা রাখবে:

* `AlarmManager`: নির্দিষ্ট class start/end time-এর কাছাকাছি trigger করার জন্য।

* `WorkManager`: deferred, retryable background work, maintenance ও periodic reconciliation-এর জন্য।

প্রতিটি minute-এ background polling করবে না।

Android exact alarms-এর permission ও policy requirements আছে। Android version এবং app-এর use case অনুযায়ী `SCHEDULE_EXACT_ALARM` অথবা উপযুক্ত alarm permission/flow যাচাই করতে হবে। Exact timing-এর permission না থাকলে user-কে জানাবে যে automation আনুমানিক সময়ে ঘটতে পারে।

Reference: Schedule alarms ।

Boot-এর পরে scheduled alarms পুনরায় register করা, timezone পরিবর্তন, device reboot, permission revoke, app update এবং manual sound-mode change—সবই test করতে হবে।

## 5.4. Local database schema

প্রথম version-এর জন্য Room-এ নিচের entities যথেষ্ট।

SQL

```
CREATE TABLE class_schedule (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    day_of_week INTEGER NOT NULL,
    start_minute INTEGER NOT NULL,
    end_minute INTEGER NOT NULL,
    sound_profile TEXT NOT NULL,
    is_enabled INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE geofence (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    radius_meters INTEGER NOT NULL,
    is_enabled INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE automation_rule (
    id TEXT PRIMARY KEY,
    schedule_id TEXT,
    geofence_id TEXT,
    priority INTEGER NOT NULL DEFAULT 0,
    restore_previous INTEGER NOT NULL DEFAULT 1,
    is_enabled INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE automation_state (
    id INTEGER PRIMARY KEY,
    original_ringer_mode INTEGER,
    active_profile TEXT,
    activation_timestamp INTEGER,
    last_transition TEXT
);
```

এটি conceptual schema; production migration, Room entities, foreign keys, indices ও validation যোগ করতে হবে।

Location data local database-এ encrypted বা appropriately protected রাখা, backup exclusion এবং user-requested deletion নিশ্চিত করা উচিত। Cloud-এ raw latitude/longitude sync করার প্রয়োজন নেই।


# 6. Automation Engine: পুরো app-এর সবচেয়ে গুরুত্বপূর্ণ অংশ

UI সুন্দর করা তুলনামূলক সহজ। কিন্তু একাধিক rule একসঙ্গে কাজ করলে, ফোনের আগের setting ঠিকভাবে restore করা এবং Android background restrictions সামলানোই আসল engineering challenge।

## 6.1. Rule priority system

একটি deterministic priority system তৈরি করো:

|
Priority

|

Profile

|

Example

|
| --- | --- | --- |
|

100

|

Emergency / user override

|

User manually turns off automation

|
|

80

|

Sleep

|

রাতের নির্ধারিত সময়

|
|

60

|

Exam / active class

|

Exam timetable

|
|

40

|

Campus

|

Campus geofence

|
|

20

|

Focus session

|

Personal study

|

এগুলো initial design values, Android-এর built-in priorities নয়।

Conflict হলে শুধু priority number দেখবে না। User override, profile expiration এবং active rule conditions বিবেচনা করবে।

একটি `RuleResolver` class সব active rules নিয়ে effective profile নির্ধারণ করবে।

## 6.2. Previous state restoration

এখানে একটি গুরুত্বপূর্ণ bug এড়াতে হবে।

ধরো:

* 9:00 AM — ফোন Normal।

* 9:30 AM — ClassMode Silent করল।

* 10:00 AM — শিক্ষার্থী নিজে manually Vibrate করল।

* 10:30 AM — ক্লাস শেষ।

এখন ClassMode যদি অন্ধভাবে Normal করে দেয়, তাহলে user-এর manual পরিবর্তন overwrite হবে।

সঠিক approach:

1. Automation শুরু হওয়ার আগে original state capture করবে।

2. App যে পরিবর্তন করেছে তার source ও timestamp record করবে।

3. Active automation চলাকালে user manual override করলে সেটি detect করার best-effort mechanism রাখবে।

4. Rule শেষ হলে original state কেবল তখনই restore করবে, যখন সেটি এখনও উপযুক্ত এবং user-এর নতুন preference-কে overwrite করার ঝুঁকি নেই।

5. নিশ্চিত না হলে silently Normal করার বদলে user-কে restoration status দেখাবে।

Android-এর সব device-এ manual change-এর perfect attribution পাওয়া সম্ভব নাও হতে পারে। তাই এটি best-effort conflict resolution হিসেবে design করবে, guaranteed rollback হিসেবে নয়।

## 6.3. Reliability testing matrix

|
Test case

|

Expected behavior

|
| --- | --- |
|

App process killed

|

System-delivered events পেলে automation পুনরায় handle করবে

|
|

Phone reboot

|

Enabled schedules ও geofences পুনরায় register হবে

|
|

Location permission revoked

|

Location rules pause এবং notification

|
|

DND permission revoked

|

DND action বন্ধ, UI-তে explanation

|
|

Battery saver active

|

Delayed event হলে status update

|
|

Timezone changed

|

Local timetable নতুন timezone-এ recalculate

|
|

Two profiles overlap

|

Deterministic rule resolver

|
|

User manually changes sound

|

Automatic restore যেন user preference overwrite না করে

|

তোমার development-এর প্রথম milestone হওয়া উচিত: একটি real Android device-এ ৭ দিন ধরে class schedule ও location automation পরীক্ষা করা, তারপর feature expansion।

# 7. SaaS Ecosystem: Server, database ও cloud কীভাবে ব্যবহার করবে?

এখানে একটা গুরুত্বপূর্ণ distinction আছে।

তোমার মূল mobile application একটি local utility। এটিকে চালানোর জন্য user-কে account খুলতে বা internet connection রাখতে বাধ্য করার দরকার নেই।

কিন্তু campus timetable sharing, multi-device sync এবং institution dashboard যোগ করলে তখন cloud backend-এর প্রয়োজন হবে।

## 7.1. Three-layer ecosystem

Layer 1 — Free, offline

Personal ClassMode

* All core automation

* Local timetable

* Local focus timer

* Personal analytics

* No account required

এটাই initial product। Internet ছাড়াও automation চলবে, যদি প্রয়োজনীয় Android services ও permissions available থাকে।

Layer 2 — Optional cloud

ClassMode Sync

* Backup and restore

* Multi-device timetable sync

* Public campus templates

* Shared class schedule links

* Account-based preferences

শুধু এই features-এর জন্য backend প্রয়োজন।

Layer 3 — B2B SaaS

ClassMode Campus

* Institution admin portal

* Department and semester templates

* Campus announcements

* Timetable publishing API

* Aggregate, opt-in product analytics

Institution-কে subscription দিয়ে এই dashboard ও schedule-management service বিক্রি করা যাবে।

## 7.2. Cloud stack

আমার প্রস্তাবিত stack:

![Working around a bug in supabase auth](https://images.openai.com/static-rsc-4/R1rAMsSo_hb3aMekWOOGi7xJ4C21URuYf3YnmzPLaP7xlYk678eE4dT_euJ2J7_zfCqQB2FgMivX5_zw9zqRm1JSiaL8VIz3ac2zpJmNs27Jort7FMhlmi9-hnyDPAXP8JJdCp-DhYy1_s6-tSIWYNqVMA_FWvIvDNR2gnszIUk?purpose=inline)

Supabase

Auth + PostgreSQL + Row Level Security

![Tải mẫu logo Cloudflare file vector AI, EPS, JPEG, SVG, PNG](https://images.openai.com/static-rsc-4/g54Zt-OvWXgtnfomi1yQ5xvnwkrkJxpz1J7KOQDO-zNOJhwYkjQQ9S9gtHctX4B0ro15qZ6RaJsFsqQmq2EzBgJTKNMrRdMfQ-u2rnD2QDnetLyk3Li5XlDoscldIbRsI-kWu6zgCVf86d-JCZw1PeOKID9HDhm8l7LL4Fl6q3s?purpose=inline)

Cloudflare Workers

Public template API, lightweight validation, rate limits

![Firebase Cloud Messaging](https://images.openai.com/static-rsc-4/ieihyXeM5-eu8Kc87-cNInjPQS6GnTIhiKwuTL0jnTzMSI0lcEJgRburSrpfQUYIUaZeRIXR_l-PUFQRSPyAmIqP1oR5FPEV5A6IjheA4fVE-IQgCROBdY7nN8tsc-Jq1jjZtrxhhKaHc4mAqjq33BiQBMLPXDUe4H1D2BiuU0s?purpose=inline)

Firebase Cloud Messaging

Optional schedule update notifications

প্রথমে Supabase-এর free tier দিয়ে শুরু করা যেতে পারে। তবে free tier-এর quota, inactivity policy, storage, egress এবং current pricing launch-এর আগে official pricing page থেকে যাচাই করবে।

Cloud database-এ personal location history, classroom entry/exit logs বা ফোনের current sound mode সংরক্ষণ করো না। এসব data তোমার core product-এর জন্য প্রয়োজন নেই।

### Proposed cloud database

|
Table

|

Purpose

|
| --- | --- |
|

`profiles`

|

Optional account preferences

|
|

`campuses`

|

Public institution information

|
|

`schedule_templates`

|

Published academic timetables

|
|

`template_versions`

|

Timetable revisions

|
|

`subscriptions`

|

Verified subscription entitlement

|
|

`institution_members`

|

Campus admin roles

|

Personal timetable cloud sync করলে user-specific schedule-এর জন্য আলাদা table এবং strict Row Level Security লাগবে।

একজন student যেন অন্য student-এর private timetable বা account data access করতে না পারে।

## 7.3. Backend API

শুরুতে মাত্র কয়েকটি endpoint যথেষ্ট:

http

```
GET    /v1/campuses
GET    /v1/campuses/:id/templates
GET    /v1/templates/:id
POST   /v1/templates/import
POST   /v1/sync
DELETE /v1/account
```

Public timetable read endpoints caching-এর মাধ্যমে serve করতে পারবে। Template update শুধু verified institution admin করতে পারবে।

নিজস্ব Node.js server, Redis, BullMQ, AI agents বা vector database এখনই দরকার নেই। তোমার এই project-এর জন্য এগুলো unnecessary complexity বাড়াবে।

# 8. Monetization: কীভাবে আয় করবে?

এই app-এর monetization এমন হওয়া উচিত, যাতে core silent automation free থাকে। কারণ একজন student প্রথমে utility হিসেবে app install করবে; subscription কিনতে নয়।

## Recommended pricing hypothesis

Free

# ৳0

মূল app, সব essential automation

* Unlimited basic time schedules

* Basic location rules

* Silent, Vibrate ও DND profiles

* Local timetable ও focus timer

* No mandatory account

ClassMode Plus

৳99–149

/ মাস

================

পরীক্ষামূলক pricing, validated price নয়

* Cloud backup ও multi-device sync

* Advanced timetable imports

* Custom profile packs

* Advanced insights

* Premium campus templates

ClassMode Campus

# Custom pricing

Institution বা university subscription

* Admin dashboard

* Department/semester schedule management

* Official timetable distribution

* Institution branding ও support

এগুলো launch price recommendation নয়; student interviews এবং willingness-to-pay tests-এর জন্য initial hypotheses।

## 8.1. Revenue models

|
Model

|

কীভাবে আয় হবে

|
| --- | --- |
|

Freemium

|

Advanced sync, imports, customization-এর জন্য paid tier

|
|

Campus SaaS

|

University/college admin portal subscription

|
|

Sponsored campus templates

|

Relevant, clearly labeled institutional sponsorship

|
|

One-time lifetime purchase

|

Premium local-only features-এর optional unlock

|

### Ads ব্যবহার করবে?

আমার পরামর্শ, প্রথম version-এ ads বাদ দাও।

এই app ব্যবহারকারীর ফোনের sound profile ও classroom focus-এর সঙ্গে সরাসরি সম্পর্কিত। Class শুরু হওয়ার সময় full-screen ad, intrusive notification বা rewarded ad-এর কারণে automation delay হলে user trust নষ্ট হবে।

যদি পরে বিজ্ঞাপন পরীক্ষা করো, তাহলে শুধু non-intrusive, optional placements রাখবে—যেমন user নিজে campus template browsing করার সময়। Automation flow, emergency access এবং settings-এ বিজ্ঞাপন রাখবে না।

এখানে subscription এবং campus B2B model বেশি স্বাভাবিক product fit হতে পারে।

## 8.2. Cost model

ধরি, ১০,০০০ monthly active users আছে।

### 10,000 MAU — illustrative cloud usage

এটি forecast বা provider quote নয়। Local-first design-এর server workload বোঝানোর উদাহরণ।

Core automation server cost

$0

Device-এর ভেতরে schedule, geofence ও sound profile processing।

Cloud sync

Usage-based

Depends on sync frequency, database rows, bandwidth, backups and provider quota.

AI/OCR server

$0 initially

Local timetable entry ও on-device parsing দিয়ে শুরু করলে AI API প্রয়োজন নেই।

তুমি যদি ১০,০০০ ব্যবহারকারীর প্রত্যেকের location server-এ পাঠাও, তাহলে network, database, privacy ও operational costs অকারণে বাড়বে। এই architecture-এ তা করার দরকার নেই।

একইভাবে ১ লাখ বা তার বেশি ব্যবহারকারী হলেও core automation-এর জন্য তোমার server scaling লাগবে না। Cloud scaling মূলত shared timetable, sync, public API এবং B2B portal-এর ওপর নির্ভর করবে।


# 9. Development process — Antigravity/Gemini দিয়ে কীভাবে বানাবে

তুমি যেহেতু AI coding tools দিয়ে project develop করো, তাই একবারে পুরো app generate না করে milestone-based development করবে।

সবচেয়ে বড় ঝুঁকি হলো AI-generated project-এ UI দেখতে সম্পূর্ণ হলেও Android permissions, background events, restoration এবং real-device behavior ঠিকমতো কাজ না করা।

## Phase 0 — Validation (প্রথম ৩–৫ দিন)

কোড লেখার আগে ২০–৩০ জন university/college student-এর সঙ্গে কথা বলো।

এই প্রশ্নগুলো করবে:

Student interview checklist

0/5

তুমি কি ক্লাসে ফোন Silent করতে ভুলে যাও?

এখন কীভাবে ফোন Silent করো—manual, built-in routines, নাকি অন্য app?

Location-based automation ব্যবহার করতে স্বচ্ছন্দ কি না?

Class timetable একবার সেট করে automation চালাতে চাও কি?

কোন feature-এর জন্য টাকা দিতে রাজি হবে?

একটি ছোট landing page তৈরি করে “Join early access” form দিতে পারো। University student groups-এ demo share করে আগ্রহ, feedback এবং sign-up rate দেখো।

এখানে লক্ষ্য হবে problem validation, paid conversion নয়।

## Phase 1 — Core Android MVP (Week 1–2)

প্রথমে মাত্র এই feature-গুলো বানাবে:

### Milestone 1: Functional MVP

Implementation tracker

0/9

Kotlin + Jetpack Compose project setup

Room database ও DataStore preferences

Manual sound profile selection

Time-based recurring schedule

DND permission onboarding

Geofence enter/exit events

Previous-state restoration

Boot/timezone rescheduling

Automation status ও local logs

এই পর্যায়ে login, payments, AI, campus portal বা backend তৈরি করবে না।

প্রথম লক্ষ্য: একটি real phone-এ time schedule ও geofence rule নির্ভরযোগ্যভাবে কাজ করছে কি না।

## Phase 2 — Student Experience (Week 3–4)

* Weekly timetable editor।

* Next class countdown।

* Quick Silent ও temporary focus mode।

* Profile conflict resolution।

* Automation health monitor।

* Schedule import-এর basic version।

* Onboarding ও user-friendly permission explanation।

এখানে অন্তত ৩–৫টি Android device model-এ testing করবে। বিশেষ করে Samsung, Xiaomi/Redmi, Realme, Pixel বা তোমার target market-এ প্রচলিত device।

## Phase 3 — Beta launch (Week 5–6)

Week 5

Closed beta

৩০–৫০ জন শিক্ষার্থীকে দিয়ে ১–২ সপ্তাহ পরীক্ষা করাও। তাদের permission setup, automation success, battery impact, missed events এবং restoration issues track করো।

Week 6

Public MVP

Bug fixes, Play Store listing, privacy policy, onboarding video, crash monitoring এবং release build।

এই timeline একটি ছোট, focused MVP-এর সম্ভাব্য পরিকল্পনা। Android-এর device-specific সমস্যা বা Play Store review-এর কারণে বেশি সময় লাগতে পারে।

## Phase 4 — Growth & Ecosystem (Month 2–4)

1. Month 2 — Campus templates

   প্রথমে কয়েকটি university-এর publicly available timetable থেকে verified templates তৈরি করো। Student-রা QR code বা link দিয়ে import করবে।

2. Month 3 — Optional sync

   Supabase Auth, PostgreSQL, backup/restore ও public template API যোগ করবে। Cloud ছাড়া core app আগের মতো চলবে।

3. Month 4 — Campus SaaS pilot

   আগ্রহী university department বা student organization-এর সঙ্গে pilot চালাও। Schedule management এবং template publishing-এর জন্য admin portal পরীক্ষা করো।

একটি বিষয় মাথায় রাখবে: university-এর timetable publish করার অনুমতি না থাকলে সেটিকে official বা verified বলে দাবি করবে না।

# 10. Antigravity-তে development-এর জন্য Master Prompt

নিচের prompt-টি Antigravity-তে project-এর initial context হিসেবে দিতে পারো। এরপর ছোট ছোট milestone prompt দিয়ে কাজ করাবে।

CLASSMODE — ANDROID PRODUCT ENGINEERING MASTER INSTRUCTIONS

# CLASSMODE — ANDROID PRODUCT ENGINEERING MASTER INSTRUCTIONS

You are the Lead Android Engineer, Software Architect, QA Engineer, and Product Engineer for ClassMode.

## Product

ClassMode is a student-first, privacy-first Android application that automatically manages a user's chosen phone sound profile based on class timetables, user-selected geofences, and scheduled focus sessions.

The app must work offline for all core functionality.

## Required stack

* Kotlin

* Jetpack Compose

* MVVM with a clean domain/data separation

* Room for local persistence

* DataStore for preferences

* Android Geofencing APIs

* AlarmManager and WorkManager where appropriate

* NotificationManager and supported Android DND APIs

* AudioManager only where permitted by the Android version and user authorization

Use stable, compatible dependencies and document their versions.

## Non-negotiable rules

1. Inspect the existing repository before modifying anything.

2. Never claim an Android API works without checking the official documentation and testing the implementation.

3. Do not use hidden APIs, root, Accessibility Service abuse, or undocumented workarounds to modify system sound settings.

4. Do not claim successful automation when the system denied or ignored an operation.

5. All automation must be explicitly configured and reversible by the user.

6. Preserve the user's previous sound settings and handle manual overrides safely.

7. Keep personal location and automation history on-device by default.

8. Do not add mandatory login, AI APIs, a paid backend, or cloud dependencies to the core automation.

9. Never collect or expose individual students' live location, attendance, or sound state to institutions.

10. Never remove existing working functionality without explaining the reason and receiving approval.

## Engineering requirements

Build the application in small, verifiable milestones.

First inspect:

* Project structure

* AndroidManifest.xml

* Gradle configuration

* SDK and dependency versions

* Existing screens, database, and tests

Then produce a concise architecture and implementation plan.

Implement:

1. App foundation and navigation.

2. Local timetable and preferences.

3. Time-based automation.

4. User-authorized geofencing.

5. DND permission onboarding.

6. Rule conflict resolution.

7. Previous-state restoration.

8. Boot and timezone rescheduling.

9. Automation health monitoring.

10. Unit tests and Android integration tests.

## Required QA

Test permission denial, app process death, device reboot, timezone changes, overlapping schedules, location exit, battery saver, manual sound changes, and unavailable DND access.

Maintain an explicit device/API compatibility matrix.

If a capability is restricted on a particular Android version or manufacturer, report it accurately and implement a safe fallback. Never bypass platform restrictions.

## Reporting after every milestone

Return:

* Files created or changed

* Architecture decisions

* Commands executed

* Build and test results

* Real-device tests actually performed

* Known bugs and limitations

* Exact next milestone

Do not invent test results, device testing, or successful builds.

Begin by inspecting the existing repository and proposing the smallest working milestone. Do not implement the entire application in a single step.

এই prompt-টি initial context হিসেবে ব্যবহার করবে। এরপর প্রত্যেক milestone-এর জন্য আলাদা prompt দেবে এবং build/test report যাচাই করবে।


# 11. কীভাবে বুঝবে app সফল হচ্ছে?

Downloads একমাত্র metric হবে না। এই app-এর ক্ষেত্রে reliability ও retention বেশি গুরুত্বপূর্ণ।

## প্রথম ৯০ দিনের target framework

নিচের সংখ্যাগুলো তোমার planning targets, industry benchmark বা guaranteed outcome নয়।

|
সময়

|

কী measure করবে

|
| --- | --- |
|

প্রথম ৩০ দিন

|

৩০–৫০ beta users, feedback, permission completion

|
|

৬০ দিন

|

২০০–৫০০ installs, weekly active users, automation reliability

|
|

৯০ দিন

|

১,০০০+ installs-এর লক্ষ্য, ৩০-day retention, paid interest

|

প্রথমে এই চারটি KPI dashboard-এ রাখবে:

Automation success rate

User-configured eligible events-এর মধ্যে কত শতাংশ ক্ষেত্রে system action সফল হয়েছে।

D7 / D30 retention

প্রথম install-এর ৭ ও ৩০ দিন পর কতজন app ব্যবহার করছে।

Restoration error rate

Automation শেষ হওয়ার পর কতবার ভুল sound profile বা unexpected state তৈরি হয়েছে।

Paid conversion

Active users-এর কত শতাংশ optional premium বা campus subscription কিনতে আগ্রহী।

ব্যক্তিগত location বা sound state server-এ না পাঠিয়েও aggregate reliability metrics সংগ্রহ করা সম্ভব, যদি user স্পষ্টভাবে opt in করে এবং telemetry যথাসম্ভব data-minimized হয়।

## 12. Google Play, privacy ও publication

এটি system settings পরিবর্তন করে, তাই user consent এবং transparent onboarding খুব গুরুত্বপূর্ণ।

Google Play-এর policy অনুযায়ী user-এর জ্ঞান ও সম্মতি ছাড়া system settings পরিবর্তন করা যাবে না; পরিবর্তনটি সহজে reversible-ও হতে হবে।

![](https://www.google.com/s2/favicons?domain=https://support.google.com\&sz=32)

Play Console Help

প্রকাশের আগে:

* Location কেন প্রয়োজন, তা permission চাওয়ার আগে ব্যাখ্যা করবে।

* Background location প্রয়োজন হলে সেটির core functionality ও policy justification স্পষ্ট করবে।

* User যেন যেকোনো সময় automation disable করতে পারে।

* Privacy policy-তে location processing, retention, deletion ও cloud sync ব্যাখ্যা করবে।

* Data Safety form-এ বাস্তবে যে data collect বা share করো, সেটিই ঘোষণা করবে।

* Institution-কে individual tracking বা student surveillance-এর সুবিধা দেবে না।

Google Play-এর location policy ও permission guidance: Google Play Developer Policy Center ।

আরও মনে রাখবে, Android 17 এবং পরবর্তী সংস্করণে background behavior পরিবর্তিত হতে পারে। তাই Play Store-এ একবার publish করেই কাজ শেষ নয়—প্রতিটি Android release-এর compatibility পরীক্ষা করতে হবে।

# আমার চূড়ান্ত product recommendation

Recommended direction

# ClassMode: Student-first, local-first

প্রথমে একটি dependable Android utility, তারপর campus productivity ecosystem।

প্রথমে বানাবে

Timetable + location + Silent/Vibrate/DND + reliable restore + automation health.

তারপর যোগ করবে

Campus templates, schedule import, focus dashboard, optional cloud sync.

Long-term business

Campus SaaS, institution schedule management এবং optional premium subscription.

প্রথম version-এর জন্য server budget: $0 লক্ষ্য।

Local-first architecture-এর কারণে cloud infrastructure ছাড়াই মূল app build, test ও launch করা সম্ভব। Optional cloud features যোগ করার পরই বাস্তব usage অনুযায়ী backend খরচ নির্ধারণ করবে।

সবচেয়ে বড় কথা: প্রথমে ৩০ জন শিক্ষার্থীর ফোনে app-টি নির্ভরযোগ্যভাবে কাজ করাও। যদি তারা ক্লাসে যাওয়ার আগে app খুলতে না হয়, permission একবার দিয়ে নিশ্চিন্তে ব্যবহার করতে পারে এবং ক্লাস শেষে ফোনের sound setting ঠিকভাবে ফিরে পায়—তাহলে তোমার ecosystem তৈরির ভিত্তি তৈরি হবে।
