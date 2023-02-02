package com.ivettevaldez.coroutines.demos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

private const val SECOND_NANO: Long = 1_000_000_000L
private const val SECOND_MS: Long = 1000L

class MyViewModel : ViewModel() {

    private var _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long> = _elapsedTime

    private var _isTrackingTime = MutableLiveData<Boolean>()
    val isTrackingTime: LiveData<Boolean> = _isTrackingTime

    fun toggleTrackTime() {
        val isTrackingTimeNow: Boolean? = isTrackingTime.value
        logger.logThreadInfo("toggleTrackTime(): isTrackingTimeNow = $isTrackingTimeNow")
        if (isTrackingTimeNow == null || !isTrackingTimeNow) {
            startTracking()
        } else {
            stopTracking()
        }
    }

    private fun startTracking() = viewModelScope.launch {
        logger.logThreadInfo("startTracking()")
        _isTrackingTime.postValue(true)
        _elapsedTime.postValue(0L)

        val startTimeNano = System.nanoTime()

        while (true) {
            val elapsedTimeSec = (System.nanoTime() - startTimeNano) / SECOND_NANO
            logger.logThreadInfo("Elapsed time: ${elapsedTime.value}")
            _elapsedTime.postValue(elapsedTimeSec)
            delay(SECOND_MS)
        }
    }

    private fun stopTracking() = viewModelScope.launch {
        logger.logThreadInfo("stopTracking()")
        _isTrackingTime.postValue(false)
        viewModelScope.coroutineContext.cancelChildren()
    }
}