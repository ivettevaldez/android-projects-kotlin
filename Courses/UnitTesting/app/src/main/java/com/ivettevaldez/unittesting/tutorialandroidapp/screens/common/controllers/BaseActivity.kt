package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers

import androidx.fragment.app.FragmentActivity
import com.ivettevaldez.unittesting.tutorialandroidapp.common.CustomApplication
import com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity : FragmentActivity() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                (application as CustomApplication).compositionRoot,
                this
            )
        }
        return controllerCompositionRoot!!
    }
}