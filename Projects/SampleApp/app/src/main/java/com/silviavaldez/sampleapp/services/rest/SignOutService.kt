package com.silviavaldez.sampleapp.services.rest

import android.content.Context
import android.util.Log
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.services.delegates.IHttpClientDelegate
import com.silviavaldez.sampleapp.services.delegates.ISignOutDelegate
import org.json.JSONObject
import java.net.HttpURLConnection

class SignOutService(private val context: Context,
                     private val delegate: ISignOutDelegate) : IHttpClientDelegate {

    private val tag = SignOutService::class.simpleName
    private val httpClientService = HttpClientService(context, this)

    fun signOut(): Boolean {
        return if (HttpClientService.networkAvailable(context)) {
            httpClientService.get(IServicesConstants.signOutUrl)
            true
        } else {
            false
        }
    }

    override fun onSuccess(result: ArrayList<Any>) {
        if (result[0] == HttpURLConnection.HTTP_OK) {
            try {
                val response = JSONObject(result[1].toString())

                if (response.has("success")) {
                    val success = response.getBoolean("success")
                    if (success) {
                        notifySuccess()
                        return
                    }
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

    private fun notifySuccess() {
        delegate.onSignOutSuccess()
    }

    private fun notifyError(error: String) {
        delegate.onSignOutFailure(error)
    }
}