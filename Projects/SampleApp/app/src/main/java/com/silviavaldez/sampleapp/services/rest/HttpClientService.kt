package com.silviavaldez.sampleapp.services.rest

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.services.delegates.IHttpClientDelegate
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.nio.charset.Charset

/**
 * Helper class for HTTP networking.
 * Created by Silvia Valdez on 20/01/2018.
 */
class HttpClientService(private val context: Context,
                        private val delegate: IHttpClientDelegate) {

    private val tag = HttpClientService::class.simpleName

    companion object {

        fun networkAvailable(context: Context): Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            return activeNetworkInfo != null
                    && activeNetworkInfo.isConnectedOrConnecting
        }
    }

    fun get(url: String) {
        Fuel.get(url)
                .header("Content-Type" to "application/json")
                .timeout(IServicesConstants.TIMEOUT)
                .response { request, response, result ->
                    Log.d(tag, "GET $url")
                    Log.d(tag, "Request: " + request.toString())

                    when (result) {
                        is Result.Failure -> {
                            val (_, error) = result
                            if (error != null) {
                                Log.e(tag, "STATUS CODE: ${response.statusCode}")
                                Log.e(tag, "RESPONSE: ${error.message}")

                                val errorMessage = getErrorMessage(response.statusCode)
                                delegate.onFailure(errorMessage)
                            }
                        }
                        is Result.Success -> {
                            val data = validateResponse(response)
                            delegate.onSuccess(data)
                        }
                    }
                }
    }

    fun post(url: String, content: String) {
        Fuel.post(url)
                .header("Content-Type" to "application/json")
                .body(content, Charset.defaultCharset())
                .timeout(IServicesConstants.TIMEOUT)
                .response { request, response, result ->
                    Log.d(tag, "POST $url")
                    Log.d(tag, "Request: " + request.toString())

                    when (result) {
                        is Result.Failure -> {
                            val (_, error) = result
                            if (error != null) {
                                Log.e(tag, "STATUS CODE: ${response.statusCode}")
                                Log.e(tag, "ERROR MESSAGE: ${error.message}")

                                val errorMessage = getErrorMessage(response.statusCode)
                                delegate.onFailure(errorMessage)
                            }
                        }
                        is Result.Success -> {
                            val data = validateResponse(response)
                            delegate.onSuccess(data)
                        }
                    }
                }
    }

    fun upload(serverUrl: String, content: List<Pair<String, Any?>>?, files: Iterable<File>) {
        Fuel.upload(serverUrl, Method.POST, content)
                .sources { _, _ ->
                    files
                }
                .timeout(IServicesConstants.TIMEOUT)
                .response { request, response, result ->
                    Log.d(tag, "POST $serverUrl")
                    Log.d(tag, "Request: " + request.toString())

                    when (result) {
                        is Result.Failure -> {
                            val (_, error) = result
                            if (error != null) {
                                Log.e(tag, "STATUS CODE: ${response.statusCode}")
                                Log.e(tag, "ERROR MESSAGE: ${error.message}")

                                val errorMessage = getErrorMessage(response.statusCode)
                                delegate.onFailure(errorMessage)
                            }
                        }
                        is Result.Success -> {
                            val data = validateResponse(response)
                            delegate.onSuccess(data)
                        }
                    }
                }
    }

    fun delete(url: String) {
        Fuel.delete(url)
                .header("Content-Type" to "application/json")
                .timeout(IServicesConstants.TIMEOUT)
                .response { request, response, result ->
                    Log.d(tag, "DELETE $url")
                    Log.d(tag, "Request: " + request.toString())

                    when (result) {
                        is Result.Failure -> {
                            val (_, error) = result
                            if (error != null) {
                                Log.e(tag, "STATUS CODE: ${response.statusCode}")
                                Log.e(tag, "ERROR MESSAGE: ${error.message}")

                                val errorMessage = getErrorMessage(response.statusCode)
                                delegate.onFailure(errorMessage)
                            }
                        }
                        is Result.Success -> {
                            val data = validateResponse(response)
                            delegate.onSuccess(data)
                        }
                    }
                }
    }

    private fun getErrorMessage(responseCode: Int): String {
        return when (responseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                context.getString(R.string.error_unauthorized) // 401
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                context.getString(R.string.error_not_found) // 404
            }

            else -> context.getString(R.string.error_default)
        }
    }

    private fun validateResponse(response: Response): ArrayList<Any> {
        // Get response's status code
        val result = ArrayList<Any>()
        result.add(response.statusCode)

        try {
            // Get response data
            val inputStream = ByteArrayInputStream(response.data)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream) as Reader?)

            var line = bufferedReader.readLine()
            val stringBuilder = StringBuilder()

            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()

            parseResponse(result, stringBuilder.toString())

            Log.d(tag, "STATUS CODE: ${response.statusCode}")
            Log.d(tag, "RESPONSE: ${result[1]}")
        } catch (ex: JSONException) {
            Log.e(tag, "Attempting to decode server's response as JSONObject", ex)
        }

        return result
    }

    private fun parseResponse(result: ArrayList<Any>, data: String) {
        try {
            val response = JSONObject(data)
            result.add(response)
        } catch (ex: Exception) {
            Log.d(tag, "JSONArray cannot be converted to JSONObject")

            val response = JSONArray(data)
            result.add(response)
        }
    }
}