package com.ivettevaldez.cleanarchitecture.screens.common.dialogs

import androidx.fragment.app.DialogFragment
import com.ivettevaldez.cleanarchitecture.common.CustomApplication
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot

abstract class BaseDialog : DialogFragment() {

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