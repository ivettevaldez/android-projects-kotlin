package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InfoDialog : BaseDialog() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: InfoController

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_BUTTON_CAPTION = "ARG_BUTTON_CAPTION"

        @JvmStatic
        fun newInfoDialog(title: String, message: String, caption: String): InfoDialog =
            InfoDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_BUTTON_CAPTION, caption)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newInfoController()

        bindArguments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewMvc = viewMvcFactory.newInfoViewMvc(null)

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
            val buttonCaption = it.getString(ARG_BUTTON_CAPTION)!!

            controller.bindArguments(title, message, buttonCaption)
        }
    }
}