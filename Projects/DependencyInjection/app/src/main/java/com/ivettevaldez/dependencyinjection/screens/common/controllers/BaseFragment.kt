package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.Injector
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.PresentationCompositionRoot

open class BaseFragment : Fragment() {

    private val compositionRoot by lazy {
        PresentationCompositionRoot(
            (requireActivity() as BaseActivity).activityCompositionRoot
        )
    }

    protected val injector get() = Injector(compositionRoot)
}