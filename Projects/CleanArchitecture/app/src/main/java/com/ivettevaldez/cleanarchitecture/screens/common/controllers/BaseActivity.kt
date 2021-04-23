package com.ivettevaldez.cleanarchitecture.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.cleanarchitecture.common.CustomApplication
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ActivityCompositionRoot
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity : AppCompatActivity() {

    private var activityCompositionRoot: ActivityCompositionRoot? = null
    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getActivityCompositionRoot(): ActivityCompositionRoot {
        if (activityCompositionRoot == null) {
            activityCompositionRoot = ActivityCompositionRoot(
                this,
                (application as CustomApplication).getCompositionRoot()
            )
        }
        return activityCompositionRoot!!
    }

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                getActivityCompositionRoot()
            )
        }
        return controllerCompositionRoot!!
    }
}