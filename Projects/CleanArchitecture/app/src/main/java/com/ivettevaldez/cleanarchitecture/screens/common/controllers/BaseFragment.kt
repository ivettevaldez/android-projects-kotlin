package com.ivettevaldez.cleanarchitecture.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot
import com.ivettevaldez.cleanarchitecture.screens.common.main.MainActivity

open class BaseFragment : Fragment() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                (requireActivity() as MainActivity).getActivityCompositionRoot()
            )
        }
        return controllerCompositionRoot!!
    }
}