package com.ivettevaldez.dependencyinjection.screens.common.controllers

/* ktlint-disable no-wildcard-imports */

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity.ActivityComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.DaggerPresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule

open class BaseFragment : Fragment() {

    private val activityComponent: ActivityComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent
    }

    private val presentationComponent: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(activityComponent))
            .build()
    }

    protected val injector get() = presentationComponent
}