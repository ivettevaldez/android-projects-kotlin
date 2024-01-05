package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views

interface ObservableViewMvc<ListenerType> : ViewMvc {

    fun registerListener(listener: ListenerType)
    fun unregisterListener(listener: ListenerType)
}