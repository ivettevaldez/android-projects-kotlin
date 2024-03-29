package com.ivettevaldez.coroutines.common

import android.os.Bundle
import com.ivettevaldez.coroutines.demos.backgroundthread.BackgroundThreadDemoFragment
import com.ivettevaldez.coroutines.demos.basiccoroutines.BasicCoroutinesDemoFragment
import com.ivettevaldez.coroutines.demos.concurrentcoroutines.ConcurrentCoroutinesDemoFragment
import com.ivettevaldez.coroutines.demos.coroutinescancellation.CoroutinesCancellationDemoFragment
import com.ivettevaldez.coroutines.demos.coroutinescancellationcooperative.CoroutinesCancellationCooperativeDemoFragment
import com.ivettevaldez.coroutines.demos.coroutinescancellationcooperative2.CoroutinesCancellationCooperative2DemoFragment
import com.ivettevaldez.coroutines.demos.design.DesignDemoFragment
import com.ivettevaldez.coroutines.demos.scopecancellation.ScopeCancellationDemoFragment
import com.ivettevaldez.coroutines.demos.scopechildrencancellation.ScopeChildrenCancellationDemoFragment
import com.ivettevaldez.coroutines.demos.uithread.UiThreadDemoFragment
import com.ivettevaldez.coroutines.demos.viewmodel.ViewModelDemoFragment
import com.ivettevaldez.coroutines.exercises.exercise1.Exercise1Fragment
import com.ivettevaldez.coroutines.exercises.exercise2.Exercise2Fragment
import com.ivettevaldez.coroutines.exercises.exercise3.Exercise3Fragment
import com.ivettevaldez.coroutines.exercises.exercise4.Exercise4Fragment
import com.ivettevaldez.coroutines.exercises.exercise5.Exercise5Fragment
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

    fun toBasicCoroutinesDemo() {
        fragNavController.pushFragment(BasicCoroutinesDemoFragment.newInstance())
    }

    fun toExercise1() {
        fragNavController.pushFragment(Exercise1Fragment.newInstance())
    }

    fun toCoroutinesCancellationDemo() {
        fragNavController.pushFragment(CoroutinesCancellationDemoFragment.newInstance())
    }

    fun toExercise2() {
        fragNavController.pushFragment(Exercise2Fragment.newInstance())
    }

    fun toConcurrentCoroutinesDemo() {
        fragNavController.pushFragment(ConcurrentCoroutinesDemoFragment.newInstance())
    }

    fun toScopeChildrenCancellationDemo() {
        fragNavController.pushFragment(ScopeChildrenCancellationDemoFragment.newInstance())
    }

    fun toExercise3() {
        fragNavController.pushFragment(Exercise3Fragment.newInstance())
    }

    fun toScopeCancellationDemo() {
        fragNavController.pushFragment(ScopeCancellationDemoFragment.newInstance())
    }

    fun toViewModelDemo() {
        fragNavController.pushFragment(ViewModelDemoFragment.newInstance())
    }

    fun toExercise4() {
        fragNavController.pushFragment(Exercise4Fragment.newInstance())
    }

    fun toDesignDemo() {
        fragNavController.pushFragment(DesignDemoFragment.newInstance())
    }

    fun toExercise5() {
        fragNavController.pushFragment(Exercise5Fragment.newInstance())
    }

    fun toCoroutinesCancellationCooperativeDemo() {
        fragNavController.pushFragment(CoroutinesCancellationCooperativeDemoFragment.newInstance())
    }

    fun toCoroutinesCancellationCooperative2Demo() {
        fragNavController.pushFragment(CoroutinesCancellationCooperative2DemoFragment.newInstance())
    }
}