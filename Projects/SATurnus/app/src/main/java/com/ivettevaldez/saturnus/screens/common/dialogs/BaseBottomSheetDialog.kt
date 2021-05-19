package com.ivettevaldez.saturnus.screens.common.dialogs

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.presentation.PresentationModule
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity

open class BaseBottomSheetDialog : BottomSheetDialogFragment() {

    private val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent
            .newPresentationComponent(
                PresentationModule()
            )
    }

    protected val injector get() = presentationComponent
}