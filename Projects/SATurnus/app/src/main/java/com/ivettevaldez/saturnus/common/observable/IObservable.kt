package com.ivettevaldez.saturnus.common.observable

interface IObservable<LISTENER_TYPE> {

    fun registerListener(listener: LISTENER_TYPE)
    fun unregisterListener(listener: LISTENER_TYPE)
}