package com.ivettevaldez.coroutines.common

import android.util.Log
import java.lang.Thread.currentThread

object ThreadInfoLogger {

    private val TAG = ThreadInfoLogger::class.simpleName

    fun logThreadInfo(message: String) {
        Log.i(
            TAG,
            "$message; thread name: ${currentThread().name}; thread ID: ${currentThread().id}"
        )
    }
}