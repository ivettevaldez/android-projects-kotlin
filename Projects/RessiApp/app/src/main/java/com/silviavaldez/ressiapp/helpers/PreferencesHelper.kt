package com.silviavaldez.ressiapp.helpers

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class for SharedPreferences management.
 * Created by Silvia Valdez on 13/2/18.
 */

const val PREFS_FILE_NAME = "com.silviavaldez.ressiapp.prefs"

const val PREFS_SESSION_ACTIVE = "PREFS_SESSION_ACTIVE"

class PreferencesHelper(context: Context) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    var activeSession: Boolean
        get() = sharedPrefs.getBoolean(PREFS_SESSION_ACTIVE, false)
        set(active) = sharedPrefs.edit().putBoolean(PREFS_SESSION_ACTIVE, active).apply()
}