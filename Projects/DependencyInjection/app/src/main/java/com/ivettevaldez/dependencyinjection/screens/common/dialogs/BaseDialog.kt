package com.ivettevaldez.dependencyinjection.screens.common.dialogs

import androidx.fragment.app.DialogFragment
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationModule
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity

open class BaseDialog : DialogFragment() {

    private val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent
            .newPresentationComponent(
                PresentationModule(this)
            )
    }

    protected val injector get() = presentationComponent
}