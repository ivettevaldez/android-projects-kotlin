package com.ivettevaldez.dependencyinjection.screens.common.controllers

/* ktlint-disable no-wildcard-imports */

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.CustomApplication
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.*

open class BaseActivity : AppCompatActivity() {

    private val appCompositionRoot: AppCompositionRoot
        get() = (application as CustomApplication).appCompositionRoot

    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this, appCompositionRoot))
            .build()
    }

    private val presentationComponent: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(activityComponent))
            .build()
    }

    protected val injector get() = Injector(presentationComponent)
}