package com.ivettevaldez.dependencyinjection.screens.common.controllers

/* ktlint-disable no-wildcard-imports */

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.CustomApplication
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.*

open class BaseActivity : AppCompatActivity() {

    private val appCompositionRoot: AppCompositionRoot
        get() = (application as CustomApplication).appCompositionRoot

    val activityCompositionRoot by lazy {
        ActivityCompositionRoot(this, appCompositionRoot)
    }

    private val presentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(activityCompositionRoot))
            .build()
    }

    protected val injector get() = Injector(presentationComponent)
}