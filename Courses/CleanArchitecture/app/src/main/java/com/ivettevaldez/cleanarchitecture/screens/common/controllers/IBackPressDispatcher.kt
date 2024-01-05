package com.ivettevaldez.cleanarchitecture.screens.common.controllers

interface IBackPressDispatcher {

    fun registerListener(listener: IBackPressedListener)
    fun unregisterListener(listener: IBackPressedListener)
}