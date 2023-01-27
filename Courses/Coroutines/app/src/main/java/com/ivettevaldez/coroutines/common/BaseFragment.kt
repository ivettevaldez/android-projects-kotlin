package com.ivettevaldez.coroutines.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ivettevaldez.coroutines.MainActivity
import com.ivettevaldez.coroutines.common.dependencyinjection.ActivityCompositionRoot

open class BaseFragment : Fragment() {

    protected open val screenTitle: String = ""

    protected lateinit var screensNavigator: ScreensNavigator

    protected val compositionRoot
        get(): ActivityCompositionRoot = (requireActivity() as MainActivity).compositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screensNavigator = compositionRoot.screensNavigator
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        compositionRoot.toolbarManipulator.let { toolbarManipulator ->
            toolbarManipulator.setScreenTitle(screenTitle)
            if (screensNavigator.isRootScreen()) {
                toolbarManipulator.hideUpButton()
            } else {
                toolbarManipulator.showUpButton()
            }
        }
    }
}