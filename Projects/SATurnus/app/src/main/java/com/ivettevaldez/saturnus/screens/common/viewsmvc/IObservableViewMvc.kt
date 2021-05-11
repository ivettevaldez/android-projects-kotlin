package com.ivettevaldez.saturnus.screens.common.viewsmvc

interface IObservableViewMvc<LISTENER_TYPE> : IViewMvc {

    fun registerListener(listener: LISTENER_TYPE)
    fun unregisterListener(listener: LISTENER_TYPE)
}