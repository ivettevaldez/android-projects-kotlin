package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.DaggerPresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.Injector
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.PresentationModule

open class BaseFragment : Fragment() {

    private val activityCompositionRoot by lazy {
        (requireActivity() as BaseActivity).activityCompositionRoot
    }

    private val presentationComponent by lazy {
        DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(activityCompositionRoot))
            .build()
    }

    protected val injector get() = Injector(presentationComponent)
}