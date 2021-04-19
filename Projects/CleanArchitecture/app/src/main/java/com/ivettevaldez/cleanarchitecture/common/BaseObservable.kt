package com.ivettevaldez.cleanarchitecture.common

/* ktlint-disable no-wildcard-imports */

import java.util.*
import kotlin.collections.HashSet

abstract class BaseObservable<ListenerType> : IObservable<ListenerType> {

    private val listeners: MutableSet<ListenerType> = HashSet()

    override fun registerListener(listener: ListenerType) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: ListenerType) {
        listeners.remove(listener)
    }

    protected fun getListeners(): Set<ListenerType> {
        return Collections.unmodifiableSet(listeners)
    }
}