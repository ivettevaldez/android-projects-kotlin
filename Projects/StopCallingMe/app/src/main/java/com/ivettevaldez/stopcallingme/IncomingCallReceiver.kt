package com.ivettevaldez.stopcallingme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.lang.reflect.Method

class IncomingCallReceiver : BroadcastReceiver() {

    private val classTag: String = this::class.java.simpleName
    private val action: String = "android.intent.action.PHONE_STATE"

    enum class IncomingCallState {

        RINGING,
        ANSWERED,
        IDLE,
        UNKNOWN
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == action && context != null) {
            try {
                val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                val extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                val state = getIncomingCallState(extraState)

                showState(context, state, number)

                if (number != null && state == IncomingCallState.RINGING) {
                    endCall(context, number)
                } else {
                    showMessage(context, "Null number: $number")
                }
            } catch (ex: Exception) {
                showMessage(context, ex.message!!)
                ex.printStackTrace()
            }
        } else {
            Log.e(classTag, "Not the expected action: ${intent?.action}")
        }
    }

    private fun getIncomingCallState(extraState: String?): IncomingCallState {
        return when {
            extraState.equals(TelephonyManager.EXTRA_STATE_RINGING, ignoreCase = true) -> {
                IncomingCallState.RINGING
            }
            extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK, ignoreCase = true) -> {
                IncomingCallState.ANSWERED
            }
            extraState.equals(TelephonyManager.EXTRA_STATE_IDLE, ignoreCase = true) -> {
                IncomingCallState.IDLE
            }
            else -> {
                IncomingCallState.UNKNOWN
            }
        }
    }

    private fun showState(context: Context, state: IncomingCallState, number: String?) {
        val message = when (state) {
            IncomingCallState.RINGING -> "Ringing from: $number"
            IncomingCallState.ANSWERED -> "Answered call from: $number"
            IncomingCallState.IDLE -> "Idle call from: $number"
            else -> "Unknown state for: $number"
        }

        showMessage(context, message)
    }

    private fun showMessage(context: Context, message: String) {
        Log.e(classTag, message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun endCall(context: Context, number: String?) {
        try {
            if (number != null) {
                val telephonyManager =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val c = Class.forName(telephonyManager.javaClass.name)
                val method: Method = c.getDeclaredMethod("getITelephony")
                method.isAccessible = true
                val telephonyService = method.invoke(telephonyManager)
                val telephonyServiceClass =
                    Class.forName(telephonyService.javaClass.name)

                val endCallMethod = telephonyServiceClass.getDeclaredMethod("endCall")
                endCallMethod.invoke(telephonyService)

                showMessage(context, "Ending the call from: $number")
            }
        } catch (ex: Exception) {
            showMessage(context, ex.message!!)
            ex.printStackTrace()
        }
    }
}