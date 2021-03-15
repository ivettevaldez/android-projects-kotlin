package com.ivettevaldez.cleanarchitecture.screens.common.views

interface IObservableViewMvc<ListenerType> : IViewMvc {

    fun registerListener(listener: ListenerType)
    fun unregisterListener(listener: ListenerType)
}