package com.ivettevaldez.unittesting.tutorialandroidapp.common.time

open class TimeProvider {

    open fun getCurrentTimestamp(): Long = System.currentTimeMillis()
}