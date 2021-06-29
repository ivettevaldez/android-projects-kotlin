package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator

import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameHelper

class ScreensNavigator(private val fragmentFrameHelper: FragmentFrameHelper) {

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }

    fun toLastActiveQuestionsList() {

    }

    fun toQuestionDetails(id: String) {

    }
}