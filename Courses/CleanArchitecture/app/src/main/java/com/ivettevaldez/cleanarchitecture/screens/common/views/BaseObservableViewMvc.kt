package com.ivettevaldez.cleanarchitecture.screens.common.views

/* ktlint-disable no-wildcard-imports */

import java.util.*
import kotlin.collections.HashSet

abstract class BaseObservableViewMvc<ListenerType> : BaseViewMvc(),
    IObservableViewMvc<ListenerType> {

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