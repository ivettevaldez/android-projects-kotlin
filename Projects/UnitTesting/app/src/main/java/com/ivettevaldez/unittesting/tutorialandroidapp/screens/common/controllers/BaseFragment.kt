package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers

import androidx.fragment.app.Fragment
import com.ivettevaldez.unittesting.tutorialandroidapp.common.CustomApplication
import com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection.ControllerCompositionRoot

open class BaseFragment : Fragment() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = ControllerCompositionRoot(
                (requireActivity().application as CustomApplication).compositionRoot,
                requireActivity()
            )
        }
        return controllerCompositionRoot!!
    }
}