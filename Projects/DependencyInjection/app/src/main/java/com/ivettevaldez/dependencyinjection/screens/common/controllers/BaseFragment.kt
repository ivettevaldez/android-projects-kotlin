package com.ivettevaldez.dependencyinjection.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.dependencyinjection.common.composition.PresentationCompositionRoot

open class BaseFragment : Fragment() {

    protected val compositionRoot by lazy {
        PresentationCompositionRoot(
            (requireActivity() as BaseActivity).activityCompositionRoot
        )
    }
}