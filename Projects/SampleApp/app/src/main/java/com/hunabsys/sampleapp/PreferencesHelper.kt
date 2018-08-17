package com.hunabsys.sampleapp

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class for SharedPreferences management.
 * Created by Silvia Valdez on 8/1/18.
 */

private const val PREFS_FILE_NAME = "com.hunabsys.smartinaco.prefs"
private const val PREFS_SESSION_ACTIVE = "PREFS_SESSION_ACTIVE"

class PreferencesHelper(context: Context) {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    var activeSession: Boolean
        get() = sharedPrefs.getBoolean(PREFS_SESSION_ACTIVE, false)
        set(active) = sharedPrefs.edit().putBoolean(PREFS_SESSION_ACTIVE, active).apply()

    fun login() {
        this.activeSession = true
    }

    fun logout() {
        this.activeSession = false
    }
}