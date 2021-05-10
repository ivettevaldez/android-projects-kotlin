package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule

open class BaseFragment : Fragment() {

    private val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent
            .newPresentationComponent(
                PresentationModule(this)
            )
    }

    protected val injector get() = presentationComponent
}