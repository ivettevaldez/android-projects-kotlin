package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers

interface BackPressDispatcher {

    fun registerListener(listener: BackPressedListener)
    fun unregisterListener(listener: BackPressedListener)
}