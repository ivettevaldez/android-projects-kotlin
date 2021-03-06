package com.ivettevaldez.saturnus.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationModule

open class BaseFragment : Fragment() {

    private val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent
            .newPresentationComponent(
                PresentationModule()
            )
    }

    protected val injector get() = presentationComponent
}