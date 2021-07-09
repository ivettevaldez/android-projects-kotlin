package com.ivettevaldez.saturnus.screens.common.controllers

import com.ivettevaldez.saturnus.screens.common.main.MainController
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import javax.inject.Inject
import javax.inject.Provider

class ControllerFactory @Inject constructor(
    private val screensNavigator: Provider<ScreensNavigator>
) {

    fun newMainController(): MainController {
        return MainController(screensNavigator.get())
    }
}