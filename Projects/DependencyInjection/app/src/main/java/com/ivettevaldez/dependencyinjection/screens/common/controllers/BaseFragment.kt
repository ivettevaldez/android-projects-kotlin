package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.DaggerPresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule

open class BaseFragment : Fragment() {

    private val presentationComponent: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .activityComponent(
                (requireActivity() as BaseActivity).activityComponent
            )
            .presentationModule(PresentationModule())
            .build()
    }

    protected val injector get() = presentationComponent
}