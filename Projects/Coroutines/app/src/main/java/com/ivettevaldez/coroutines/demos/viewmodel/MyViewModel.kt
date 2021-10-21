package com.ivettevaldez.coroutines.demos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long> = _elapsedTime

    private val _isTrackingTime = MutableLiveData<Boolean>()
    val isTrackingTime: LiveData<Boolean> = _isTrackingTime

    companion object {

        private const val SECOND: Long = 1_000_000_000L
        private const val DELAY: Long = 1000L
    }

    fun toggleTrackElapsedTime() {
        val isTrackingTimeNow = isTrackingTime.value
        logThreadInfo("toggleTrackElapsedTime(): isTrackingTimeNow -> $isTrackingTimeNow")

        if (isTrackingTimeNow == null || !isTrackingTimeNow) {
            startTrackingTime()
        } else {
            stopTrackingTime()
        }
    }

    private fun startTrackingTime() = viewModelScope.launch {
        logThreadInfo("startTrackingTime()")
        _isTrackingTime.postValue(true)

        val startNanoTime = System.nanoTime()

        while (true) {
            val elapsedTime = (System.nanoTime() - startNanoTime) / SECOND
            _elapsedTime.postValue(elapsedTime)
            logThreadInfo("Elapsed time: $elapsedTime")
            delay(DELAY)
        }
    }

    private fun stopTrackingTime() {
        logThreadInfo("stopTrackingTime()")
        _isTrackingTime.postValue(false)
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}