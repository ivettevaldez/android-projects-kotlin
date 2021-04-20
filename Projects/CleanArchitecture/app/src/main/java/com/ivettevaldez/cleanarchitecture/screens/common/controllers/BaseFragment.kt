package com.ivettevaldez.cleanarchitecture.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.cleanarchitecture.common.CustomApplication
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot

open class BaseFragment : Fragment() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                requireActivity(),
                (requireActivity().application as CustomApplication).getCompositionRoot()
            )
        }
        return controllerCompositionRoot!!
    }
}