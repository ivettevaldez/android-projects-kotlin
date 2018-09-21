package com.silviavaldez.sampleapp.services.rest

import android.content.Context
import android.util.Log
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.JsonValidationHelper
import com.silviavaldez.sampleapp.services.delegates.IHttpClientDelegate
import com.silviavaldez.sampleapp.services.delegates.ISignInDelegate
import org.json.JSONObject
import java.net.HttpURLConnection

class SignInService(private val context: Context) : IHttpClientDelegate {

    private val tag = SignInService::class.simpleName
    private val httpClientService = HttpClientService(context, this)

    fun signIn(credentials: String): Boolean {
        return if (HttpClientService.networkAvailable(context)) {
            httpClientService.post(IServicesConstants.signInUrl, credentials)
            true
        } else {
            false
        }
    }

    override fun onSuccess(result: ArrayList<Any>) {
        if (result[0] == HttpURLConnection.HTTP_OK) {
            try {
                val response = JSONObject(result[1].toString())
                val success = validateResult(response)
                if (success) {
                    notifySuccess()
                    return
                }
            } catch (ex: Exception) {
                Log.e(tag, "Attempting to validate response", ex)
            }
        }

        val defaultError = context.getString(R.string.error_default)
        notifyError(defaultError)
    }

    override fun onFailure(error: String) {
        notifyError(error)
    }

    private fun validateResult(result: JSONObject): Boolean {
        if (result.has("data")) {
            val data = result.getJSONObject("data")

            val firstName = JsonValidationHelper().getStringValue(data, "first_name")
            val lastName = JsonValidationHelper().getStringValue(data, "last_name")

            Log.e(tag, "------> Hello $firstName $lastName!")
            return true
        } else {
            Log.e(tag, "No key 'data' was found in: $result")
        }
        return false
    }

    private fun notifySuccess() {
        val delegate = context as ISignInDelegate
        delegate.onSignInSuccess()
    }

    private fun notifyError(error: String) {
        val delegate = context as ISignInDelegate
        delegate.onSignInFailure(error)
    }
}