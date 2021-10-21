package com.ivettevaldez.coroutines.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ivettevaldez.coroutines.demos.basiccoroutine.BasicCoroutineDemoFragment
import com.ivettevaldez.coroutines.demos.scopecancellation.ScopeCancellationDemoFragment
import com.ivettevaldez.coroutines.demos.threads.ThreadsDemoFragment
import com.ivettevaldez.coroutines.demos.viewmodel.ViewModelDemoFragment
import com.ivettevaldez.coroutines.exercises.exercise1.Exercise1Fragment
import com.ivettevaldez.coroutines.exercises.exercise2.Exercise2Fragment
import com.ivettevaldez.coroutines.home.HomeFragment
import com.ncapdevi.fragnav.FragNavController

class ScreensNavigator(private val fragNavController: FragNavController) {

    fun init(savedInstanceState: Bundle?) {
        fragNavController.rootFragmentListener = object : FragNavController.RootFragmentListener {
            override val numberOfRootFragments: Int get() = 1

            override fun getRootFragment(index: Int): Fragment = HomeFragment.newInstance()
        }

        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
    }

    fun isRootScreen(): Boolean = fragNavController.isRootFragment

    fun onSaveInstanceState(outState: Bundle?) {
        fragNavController.onSaveInstanceState(outState)
    }

    fun navigateUp() {
        fragNavController.popFragment()
    }

    fun navigateBack(): Boolean {
        return if (isRootScreen()) {
            false
        } else {
            navigateUp()
            true
        }
    }

    fun toHomeScreen() {
        fragNavController.clearStack()
        fragNavController.pushFragment(HomeFragment.newInstance())
    }

    fun toThreadsDemo() {
        fragNavController.pushFragment(ThreadsDemoFragment.newInstance())
    }

    fun toBasicCoroutineDemo() {
        fragNavController.pushFragment(BasicCoroutineDemoFragment.newInstance())
    }

    fun toExercise1() {
        fragNavController.pushFragment(Exercise1Fragment.newInstance())
    }

    fun toExercise2() {
        fragNavController.pushFragment(Exercise2Fragment.newInstance())
    }

    fun toScopeCancellationDemo() {
        fragNavController.pushFragment(ScopeCancellationDemoFragment.newInstance())
    }

    fun toViewModelDemo() {
        fragNavController.pushFragment(ViewModelDemoFragment.newInstance())
    }
}