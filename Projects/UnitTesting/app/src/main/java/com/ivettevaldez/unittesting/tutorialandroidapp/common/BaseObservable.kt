package com.ivettevaldez.unittesting.tutorialandroidapp.common

import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class BaseObservable<LISTENER_TYPE> {

    protected val listeners: MutableSet<LISTENER_TYPE> =
        Collections.newSetFromMap(ConcurrentHashMap())

    fun registerListener(listener: LISTENER_TYPE) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_TYPE) {
        listeners.remove(listener)
    }
}