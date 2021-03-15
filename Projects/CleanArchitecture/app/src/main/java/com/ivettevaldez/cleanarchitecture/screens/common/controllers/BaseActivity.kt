package com.ivettevaldez.cleanarchitecture.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.cleanarchitecture.common.CustomApplication
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity : AppCompatActivity() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                this,
                (application as CustomApplication).getCompositionRoot()
            )
        }
        return controllerCompositionRoot!!
    }
}