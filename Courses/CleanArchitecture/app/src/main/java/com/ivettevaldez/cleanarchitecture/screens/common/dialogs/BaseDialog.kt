package com.ivettevaldez.cleanarchitecture.screens.common.dialogs

import androidx.fragment.app.DialogFragment
import com.ivettevaldez.cleanarchitecture.common.dependencyinjection.ControllerCompositionRoot
import com.ivettevaldez.cleanarchitecture.screens.common.main.MainActivity

abstract class BaseDialog : DialogFragment() {

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