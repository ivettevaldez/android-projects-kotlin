package com.silviavaldez.sampleapp.helpers

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class for SharedPreferences management.
 * Created by Silvia Valdez on 8/1/18.
 */
private const val PREFS_FILE_NAME = "com.silviavaldez.sampleapp.prefs"

private const val PREFS_SESSION_ACTIVE = "PREFS_SESSION_ACTIVE"
private const val PREFS_USER_ID = "PREF_USER_ID"
private const val PREFS_FIRST_NAME = "PREFS_FIRST_NAME"
private const val PREFS_LAST_NAME = "PREFS_LAST_NAME"

class PreferencesHelper(context: Context) {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    var activeSession: Boolean
        get() = sharedPrefs.getBoolean(PREFS_SESSION_ACTIVE, false)
        set(active) = sharedPrefs.edit().putBoolean(PREFS_SESSION_ACTIVE, active).apply()

    var userId: Long
        get() = sharedPrefs.getLong(PREFS_USER_ID, 0)
        set(id) = sharedPrefs.edit().putLong(PREFS_USER_ID, id).apply()

    private var firstName: String
        get() = sharedPrefs.getString(PREFS_FIRST_NAME, "")!!
        set(name) = sharedPrefs.edit().putString(PREFS_FIRST_NAME, name).apply()

    private var lastName: String
        get() = sharedPrefs.getString(PREFS_LAST_NAME, "")!!
        set(name) = sharedPrefs.edit().putString(PREFS_LAST_NAME, name).apply()

    fun getUserName(): String {
        return "$firstName $lastName"
    }

    fun saveUser(id: Long, firstName: String, lastName: String) {
        this.userId = id
        this.firstName = firstName
        this.lastName = lastName
    }

    fun login() {
        this.activeSession = true
    }

    fun logout() {
        sharedPrefs.edit().clear().apply()
    }
}