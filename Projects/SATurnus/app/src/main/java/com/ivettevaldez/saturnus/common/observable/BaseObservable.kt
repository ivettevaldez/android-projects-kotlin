package com.ivettevaldez.saturnus.common.observable

open class BaseObservable<LISTENER_TYPE> : IObservable<LISTENER_TYPE> {

    protected val listeners: MutableSet<LISTENER_TYPE> = HashSet()

    override fun registerListener(listener: LISTENER_TYPE) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: LISTENER_TYPE) {
        listeners.remove(listener)
    }
}