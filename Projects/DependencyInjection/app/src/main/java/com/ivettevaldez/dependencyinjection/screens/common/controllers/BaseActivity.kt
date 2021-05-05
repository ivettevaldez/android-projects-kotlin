package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.application.DaggerApplicationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.UseCasesModule

open class BaseActivity : AppCompatActivity() {

    private val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(application))
            .build()
    }

    val activityComponent: ActivityComponent by lazy {
        applicationComponent.newActivityComponent(ActivityModule(this))
    }

    private val presentationComponent: PresentationComponent by lazy {
        activityComponent.newPresentationComponent(
            PresentationModule(),
            UseCasesModule()
        )
    }

    protected val injector get() = presentationComponent
}