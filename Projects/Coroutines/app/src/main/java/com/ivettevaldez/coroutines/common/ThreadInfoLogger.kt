package com.ivettevaldez.coroutines.common

import android.util.Log

object ThreadInfoLogger {

    private val classTag: String = this::class.java.simpleName

    fun logThreadInfo(message: String) {
        Log.i(
            classTag,
            "$message - thread name: ${Thread.currentThread().name} - " +
                    "thread ID: ${Thread.currentThread().id}"
        )
    }
}