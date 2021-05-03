package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.CustomApplication
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.ActivityCompositionRoot
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.AppCompositionRoot
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.Injector
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.PresentationCompositionRoot

open class BaseActivity : AppCompatActivity() {

    private val appCompositionRoot: AppCompositionRoot
        get() = (application as CustomApplication).appCompositionRoot

    val activityCompositionRoot by lazy {
        ActivityCompositionRoot(this, appCompositionRoot)
    }

    private val compositionRoot by lazy {
        PresentationCompositionRoot(activityCompositionRoot)
    }

    protected val injector get() = Injector(compositionRoot)
}