package com.cryptxploit.classmode.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val DEFAULT_PROFILE = stringPreferencesKey("default_profile")
        val IS_AUTOMATION_ENABLED = booleanPreferencesKey("is_automation_enabled")
        val USER_OVERRIDE = stringPreferencesKey("user_override")
        val OVERRIDE_EXPIRY_TIME = longPreferencesKey("override_expiry_time")
        val KEY_DIAGNOSTICS_OPT_IN = booleanPreferencesKey("diagnostics_opt_in")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val LAST_RESOLVED_PROFILE = stringPreferencesKey("last_resolved_profile")
    }

    val isDiagnosticsOptInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DIAGNOSTICS_OPT_IN] ?: false
    }

    val defaultProfileFlow: Flow<SoundProfile> = context.dataStore.data.map { preferences ->
        val profileName = preferences[DEFAULT_PROFILE] ?: SoundProfile.NORMAL.name
        SoundProfile.valueOf(profileName)
    }

        val userOverrideFlow: Flow<SoundProfile?> = context.dataStore.data.map { preferences ->
        val overrideName = preferences[USER_OVERRIDE]
        if (overrideName != null) SoundProfile.valueOf(overrideName) else null
    }

    val overrideExpiryTimeFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[OVERRIDE_EXPIRY_TIME] ?: 0L
    }

    val isAutomationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_AUTOMATION_ENABLED] ?: true
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "System"
    }

    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "System"
    }

    val isHapticsEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAPTICS_ENABLED] ?: true
    }

    suspend fun setDefaultProfile(profile: SoundProfile) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_PROFILE] = profile.name
        }
    }

    suspend fun setAutomationEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTOMATION_ENABLED] = isEnabled
        }
    }

    suspend fun setDiagnosticsOptIn(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DIAGNOSTICS_OPT_IN] = enabled
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = lang
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }

        suspend fun setUserOverride(profile: SoundProfile?, expiryTime: Long = 0L) {
        context.dataStore.edit { preferences ->
            if (profile != null) {
                preferences[USER_OVERRIDE] = profile.name
                preferences[OVERRIDE_EXPIRY_TIME] = expiryTime
            } else {
                preferences.remove(USER_OVERRIDE)
                preferences.remove(OVERRIDE_EXPIRY_TIME)
            }
        }
    }
}


