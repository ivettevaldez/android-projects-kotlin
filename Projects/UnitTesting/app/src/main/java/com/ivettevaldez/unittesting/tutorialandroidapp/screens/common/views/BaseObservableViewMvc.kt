package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views

import java.util.*
import kotlin.collections.HashSet

class BaseObservableViewMvc<ListenerType> : BaseViewMvc(), ObservableViewMvc<ListenerType> {

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