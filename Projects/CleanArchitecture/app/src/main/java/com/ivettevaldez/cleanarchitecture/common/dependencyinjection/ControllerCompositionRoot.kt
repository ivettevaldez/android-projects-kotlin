package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionsUseCase
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsManager
import com.ivettevaldez.cleanarchitecture.screens.common.fragmentframehelper.FragmentFrameHelper
import com.ivettevaldez.cleanarchitecture.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreensNavigator

class ControllerCompositionRoot(
    private val activity: FragmentActivity,
    private val compositionRoot: CompositionRoot
) {

    private fun getActivity(): FragmentActivity = activity

    private fun getContext(): Context = activity.applicationContext

    private fun getStackOverflowApi(): StackOverflowApi = compositionRoot.getStackOverflowApi()

    private fun getFragmentManager(): FragmentManager = getActivity().supportFragmentManager

    private fun getFragmentFrameWrapper(): IFragmentFrameWrapper =
        getActivity() as IFragmentFrameWrapper

    private fun getLayoutInflater(): LayoutInflater = LayoutInflater.from(getActivity())

    private fun getNavDrawerHelper(): INavDrawerHelper = getActivity() as INavDrawerHelper

    private fun getFragmentFrameHelper(): FragmentFrameHelper {
        return FragmentFrameHelper(
            getActivity(),
            getFragmentFrameWrapper(),
            getFragmentManager()
        )
    }

    fun getScreensNavigator(): ScreensNavigator {
        return ScreensNavigator(getFragmentFrameHelper())
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(
            getLayoutInflater(),
            getNavDrawerHelper()
        )
    }

    fun getDialogsManager(): DialogsManager {
        return DialogsManager(
            getContext(),
            getFragmentManager()
        )
    }

    fun getDialogsEventBus(): DialogsEventBus {
        return compositionRoot.getDialogsEventBus()
    }

    fun getFetchQuestionsUseCase(): FetchQuestionsUseCase {
        return FetchQuestionsUseCase(getStackOverflowApi())
    }

    fun getFetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase {
        return FetchQuestionDetailsUseCase(getStackOverflowApi())
    }
}