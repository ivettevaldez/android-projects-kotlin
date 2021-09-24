package com.ivettevaldez.coroutines.common.dependencyinjection

import androidx.fragment.app.FragmentActivity
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.ScreensNavigator
import com.ivettevaldez.coroutines.common.ToolbarDelegate
import com.ivettevaldez.coroutines.exercises.GetReputationEndpoint
import com.ncapdevi.fragnav.FragNavController

class ActivityCompositionRoot(
    private val activity: FragmentActivity,
    private val applicationCompositionRoot: ApplicationCompositionRoot
) {

    val toolbarManipulator get() = activity as ToolbarDelegate

    private val fragmentManager get() = activity.supportFragmentManager

    private val fragNavController
        get() = FragNavController(
            fragmentManager,
            R.id.main_frame_content
        )

    val screensNavigator: ScreensNavigator by lazy {
        ScreensNavigator(fragNavController)
    }

    val getReputationEndpoint get() = GetReputationEndpoint()
}