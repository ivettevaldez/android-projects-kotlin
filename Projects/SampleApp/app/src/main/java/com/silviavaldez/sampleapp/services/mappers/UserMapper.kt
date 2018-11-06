package com.silviavaldez.sampleapp.services.mappers

import android.content.Context
import android.util.Log
import com.silviavaldez.sampleapp.helpers.JsonValidationHelper
import com.silviavaldez.sampleapp.helpers.PreferencesHelper
import org.json.JSONObject

class UserMapper {

    private val tag = UserMapper::class.simpleName

    fun map(context: Context, result: JSONObject): Boolean {
        if (result.has("data")) {
            val data = result.getJSONObject("data")

            val id = JsonValidationHelper().getLongValue(data, "id")
            val firstName = JsonValidationHelper().getStringValue(data, "first_name")
            val lastName = JsonValidationHelper().getStringValue(data, "last_name")

            saveSession(context, id, firstName, lastName)
            return true
        } else {
            Log.e(tag, "No key 'data' was found in: $result")
        }
        return false
    }

    private fun saveSession(context: Context, id: Long, firstName: String, lastName: String) {
        val preferencesHelper = PreferencesHelper(context)

        preferencesHelper.saveUser(id, firstName, lastName)
        preferencesHelper.login()

        Log.d(tag, "-------> USER: ID - $id, NAME - $firstName $lastName")
    }
}