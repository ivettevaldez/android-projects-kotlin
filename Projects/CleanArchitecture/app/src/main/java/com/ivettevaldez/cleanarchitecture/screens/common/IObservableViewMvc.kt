package com.ivettevaldez.cleanarchitecture.screens.common

interface IObservableViewMvc<ListenerType> : IViewMvc {

    fun registerListener(listener: ListenerType)
    fun unregisterListener(listener: ListenerType)
}