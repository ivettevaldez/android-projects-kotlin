package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PromptDialog : BaseDialog() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: PromptController

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_CAPTION = "ARG_POSITIVE_CAPTION"
        private const val ARG_NEGATIVE_CAPTION = "ARG_NEGATIVE_CAPTION"

        fun newPromptDialog(
            title: String,
            message: String,
            positiveCaption: String,
            negativeCaption: String
        ): PromptDialog = PromptDialog().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_CAPTION, positiveCaption)
                putString(ARG_NEGATIVE_CAPTION, negativeCaption)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newPromptController()

        bindArguments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewMvc = viewMvcFactory.newPromptViewMvc(null)

        val dialog = Dialog(requireContext())
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

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
            val message = it.getString(ARG_MESSAGE)!!
            val positiveCaption = it.getString(ARG_POSITIVE_CAPTION)!!
            val negativeCaption = it.getString(ARG_NEGATIVE_CAPTION)!!

            controller.bindArguments(title, message, positiveCaption, negativeCaption)
        }
    }
}