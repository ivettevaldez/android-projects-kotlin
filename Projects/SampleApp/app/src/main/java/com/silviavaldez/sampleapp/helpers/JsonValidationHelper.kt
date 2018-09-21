package com.silviavaldez.sampleapp.helpers

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

/**
 * Helper class to validate JSON Objects.
 * Created by Silvia Valdez on 1/22/18.
 */
class JsonValidationHelper {

    val tag = JsonValidationHelper::class.simpleName

    /**
     * Gets a String value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key The name of the value.
     * @return A string value.
     */
    fun getStringValue(receivedJSONObject: JSONObject, key: String): String {
        val valueStr: String?

        try {
            if (receivedJSONObject.has(key)) {
                valueStr = receivedJSONObject.getString(key)
                if (valueStr != null && "null" != valueStr) {
                    return valueStr
                }
            } else {
                Log.e(tag, String.format("No key '%s' was found in: %s", key, receivedJSONObject))
            }
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to get a String value from JSON", ex)
        }

        return ""
    }

    /**
     * Gets an Integer value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key The name of the value.
     * @return An integer value.
     */
    fun getIntValue(receivedJSONObject: JSONObject, key: String): Int {
        try {
            if (receivedJSONObject.has(key)) {
                return receivedJSONObject.getInt(key)
            } else {
                Log.e(tag, String.format("No key '%s' was found in: %s", key, receivedJSONObject))
            }
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to get a Integer value from JSON", ex)
        }

        return 0
    }

    /**
     * Gets an Integer value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key The name of the value.
     * @return An integer value.
     */
    fun getLongValue(receivedJSONObject: JSONObject, key: String): Long {
        try {
            if (receivedJSONObject.has(key)) {
                return receivedJSONObject.getLong(key)
            } else {
                Log.e(tag, String.format("No key '%s' was found in: %s", key, receivedJSONObject))
            }
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to get a Long value from JSON", ex)
        }

        return 0L
    }

    /**
     * Gets an Integer value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key The name of the value.
     * @return A double value.
     */
    fun getDoubleValue(receivedJSONObject: JSONObject, key: String): Double {
        try {
            if (receivedJSONObject.has(key)) {
                return receivedJSONObject.getDouble(key)
            } else {
                Log.e(tag, String.format("No key '%s' was found in: %s", key, receivedJSONObject))
            }
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to get a Double value from JSON", ex)
        }

        return 0.0
    }

    /**
     * Gets an Integer value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key The name of the value.
     * @return A double value.
     */
    fun getBooleanValue(receivedJSONObject: JSONObject, key: String): Boolean {
        try {
            if (receivedJSONObject.has(key)) {
                return receivedJSONObject.getBoolean(key)
            } else {
                Log.e(tag, String.format("No key '%s' was found in: %s", key, receivedJSONObject))
            }
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to get a Boolean value from JSON", ex)
        }

        return false
    }
}