package com.ivettevaldez.dependencyinjection.screens.common.controllers

/* ktlint-disable no-wildcard-imports */

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.DaggerActivityComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.DaggerApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.DaggerPresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {

    private val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(application))
            .build()
    }

    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this, applicationComponent))
            .build()
    }

    private val presentationComponent: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(activityComponent))
            .build()
    }

    protected val injector get() = presentationComponent
}