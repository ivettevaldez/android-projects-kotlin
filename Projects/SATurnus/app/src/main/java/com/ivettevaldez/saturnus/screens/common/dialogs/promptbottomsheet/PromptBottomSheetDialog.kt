package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PromptBottomSheetDialog : BaseBottomSheetDialog() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: PromptBottomSheetController

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_OPTION_ONE_CAPTION = "ARG_OPTION_ONE_CAPTION"
        private const val ARG_OPTION_TWO_CAPTION = "ARG_OPTION_TWO_CAPTION"

        @JvmStatic
        fun newPromptBottomSheetDialog(
            title: String,
            optionOneCaption: String,
            optionTwoCaption: String
        ): PromptBottomSheetDialog =
            PromptBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_OPTION_ONE_CAPTION, optionOneCaption)
                    putString(ARG_OPTION_TWO_CAPTION, optionTwoCaption)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newPromptBottomSheetController()

        bindArguments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewMvc = viewMvcFactory.newPromptBottomSheetViewMvc(null)
        val dialog = BottomSheetDialog(requireContext())

        controller.bindDialogAndView(dialog, viewMvc)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }

    private fun bindArguments() {
        requireArguments().let {
            val title = it.getString(ARG_TITLE)!!
            val optionOneCaption = it.getString(ARG_OPTION_ONE_CAPTION)!!
            val optionTwoCaption = it.getString(ARG_OPTION_TWO_CAPTION)!!

            controller.bindArguments(title, optionOneCaption, optionTwoCaption)
        }
    }
}