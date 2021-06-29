package com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list.FetchLastActiveQuestionsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchLastActiveQuestionsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.ViewMvcFactory
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BackPressDispatcher
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts.ToastsHelper

class ControllerCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity
) {

    private fun getContext(): Context = activity

    private fun getActivity(): FragmentActivity = activity

    private fun getFragmentManager(): FragmentManager = activity.supportFragmentManager

    private fun getFragmentFrameHelper(): FragmentFrameHelper {
        return FragmentFrameHelper(getActivity(), getFragmentFrameWrapper(), getFragmentManager())
    }

    private fun getFragmentFrameWrapper() = activity as FragmentFrameWrapper

    private fun getLayoutInflater(): LayoutInflater = LayoutInflater.from(getContext())

    private fun getNavDrawerHelper(): NavDrawerHelper = activity as NavDrawerHelper

    private fun getStackOverflowApi(): StackOverflowApi = compositionRoot.getStackoverflowApi()

    fun getTimeProvider(): TimeProvider = compositionRoot.getTimeProvider()

    fun getViewMvcFactory() = ViewMvcFactory(getLayoutInflater(), getNavDrawerHelper())

    fun getToastHelper(): ToastsHelper = ToastsHelper(getContext())

    fun getScreensNavigator(): ScreensNavigator = ScreensNavigator(getFragmentFrameHelper())

    fun getBackPressDispatcher() = activity as BackPressDispatcher

    private fun getFetchLastActiveQuestionsEndpoint() =
        FetchLastActiveQuestionsEndpoint(getStackOverflowApi())

    fun fetchLastActiveQuestionsUseCase() =
        FetchLastActiveQuestionsUseCase(getFetchLastActiveQuestionsEndpoint())

    fun getFetchQuestionDetailsEndpoint() = FetchQuestionDetailsEndpoint(getStackOverflowApi())

    fun fetchQuestionDetailsUseCase() = FetchQuestionDetailsUseCase()
}