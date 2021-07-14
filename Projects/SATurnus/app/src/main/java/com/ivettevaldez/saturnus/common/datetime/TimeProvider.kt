package com.ivettevaldez.saturnus.common.datetime

import javax.inject.Inject

open class TimeProvider @Inject constructor() {

    fun getCurrentTimestamp(): Long = System.currentTimeMillis()
}