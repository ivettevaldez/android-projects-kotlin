package com.ivettevaldez.saturnus.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.saturnus.common.CustomApplication
import com.ivettevaldez.saturnus.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {

    private val applicationComponent by lazy {
        (application as CustomApplication).applicationComponent
    }

    val activityComponent: ActivityComponent by lazy {
        applicationComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    private val presentationComponent: PresentationComponent by lazy {
        activityComponent.newPresentationComponent(
            PresentationModule()
        )
    }

    protected val injector get() = presentationComponent
}