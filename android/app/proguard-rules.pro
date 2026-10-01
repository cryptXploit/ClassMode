# Preserve Room database models and DAOs
-keep class com.cryptxploit.classmode.data.local.entity.** { *; }
-keep class com.cryptxploit.classmode.data.local.dao.** { *; }

# Preserve DataStore preferences models
-keep class com.cryptxploit.classmode.data.preferences.** { *; }

# Preserve Coroutines Service Loader
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Google AdMob SDK specific rules
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Ensure domain enums are not obfuscated to break DataStore/Room mapping
-keepclassmembers enum com.cryptxploit.classmode.domain.model.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve System Receivers declared in AndroidManifest
-keep class com.cryptxploit.classmode.data.receiver.** { *; }

