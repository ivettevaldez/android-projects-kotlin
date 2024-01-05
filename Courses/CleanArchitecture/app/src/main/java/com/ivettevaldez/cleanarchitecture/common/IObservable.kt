package com.ivettevaldez.cleanarchitecture.common

interface IObservable<ListenerType> {

    fun registerListener(listener: ListenerType)
    fun unregisterListener(listener: ListenerType)
}