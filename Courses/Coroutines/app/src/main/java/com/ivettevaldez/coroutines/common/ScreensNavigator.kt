package com.ivettevaldez.coroutines.common

import android.os.Bundle
import com.ivettevaldez.coroutines.demos.backgroundthread.BackgroundThreadDemoFragment
import com.ivettevaldez.coroutines.demos.uithread.UiThreadDemoFragment
import com.ivettevaldez.coroutines.home.HomeFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavController.RootFragmentListener

class ScreensNavigator(private val fragNavController: FragNavController) {

    fun init(savedInstanceState: Bundle?) {
        fragNavController.rootFragmentListener = object : RootFragmentListener {
            override val numberOfRootFragments: Int get() = 1

            override fun getRootFragment(index: Int) = HomeFragment.newInstance()
        }
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
    }

    fun onSaveInstanceState(outState: Bundle?) {
        fragNavController.onSaveInstanceState(outState)
    }

    fun isRootScreen(): Boolean = fragNavController.isRootFragment

    fun navigateBack(): Boolean = if (fragNavController.isRootFragment) {
        false
    } else {
        fragNavController.popFragment()
        true
    }

    fun navigateUp() {
        fragNavController.popFragment()
    }

    fun toUiThreadDemo() {
        fragNavController.pushFragment(UiThreadDemoFragment.newInstance())
    }

    fun toBackgroundThreadDemo() {
        fragNavController.pushFragment(BackgroundThreadDemoFragment.newInstance())
    }
}