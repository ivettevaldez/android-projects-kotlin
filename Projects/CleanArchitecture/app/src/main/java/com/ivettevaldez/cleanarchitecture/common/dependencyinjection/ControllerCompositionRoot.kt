package com.ivettevaldez.cleanarchitecture.common.dependencyinjection

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionsUseCase
import com.ivettevaldez.cleanarchitecture.screens.common.ViewMvcFactory
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressDispatcher
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

class ControllerCompositionRoot(
    private val activity: FragmentActivity,
    private val compositionRoot: CompositionRoot
) {

    private var screenNavigator: ScreenNavigator? = null

    private fun getActivity(): FragmentActivity = activity

    private fun getFragmentManager(): FragmentManager = getActivity().supportFragmentManager

    private fun getFragmentFrameWrapper(): IFragmentFrameWrapper =
        getActivity() as IFragmentFrameWrapper

    private fun getLayoutInflater(): LayoutInflater = LayoutInflater.from(getActivity())

    private fun getStackOverflowApi(): StackOverflowApi {
        return compositionRoot.getStackOverflowApi()
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }

    fun getScreenNavigator(): ScreenNavigator {
        if (screenNavigator == null) {
            screenNavigator = ScreenNavigator(
                getFragmentManager(),
                getFragmentFrameWrapper()
            )
        }
        return screenNavigator!!
    }

    fun getFetchQuestionsUseCase(): FetchQuestionsUseCase {
        return FetchQuestionsUseCase(getStackOverflowApi())
    }

    fun getFetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase {
        return FetchQuestionDetailsUseCase(getStackOverflowApi())
    }

    fun getBackPressDispatcher(): IBackPressDispatcher {
        return getActivity() as IBackPressDispatcher
    }
}