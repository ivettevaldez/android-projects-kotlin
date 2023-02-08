package com.ivettevaldez.coroutines.common.dependencyinjection

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.ScreensNavigator
import com.ivettevaldez.coroutines.common.ToolbarDelegate
import com.ivettevaldez.coroutines.demos.design.BenchmarkUseCase
import com.ivettevaldez.coroutines.exercises.exercise1.GetReputationEndpoint
import com.ivettevaldez.coroutines.exercises.exercise4.FactorialUseCase
import com.ncapdevi.fragnav.FragNavController

class ActivityCompositionRoot(
    private val activity: FragmentActivity,
    private val applicationCompositionRoot: ApplicationCompositionRoot
) {

    val toolbarManipulator get() = activity as ToolbarDelegate

    val screensNavigator: ScreensNavigator by lazy {
        ScreensNavigator(fragNavController)
    }

    private val fragmentManager: FragmentManager get() = activity.supportFragmentManager

    private val fragNavController get() = FragNavController(fragmentManager, R.id.frame_content)

    val getReputationEndpoint get() = GetReputationEndpoint()

    val factorialUseCase: FactorialUseCase get() = FactorialUseCase()
    
    val benchmarkUseCase: BenchmarkUseCase get() = BenchmarkUseCase()
}