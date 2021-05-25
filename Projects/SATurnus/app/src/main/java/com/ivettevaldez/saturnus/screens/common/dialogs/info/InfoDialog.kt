package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class InfoDialog : BaseDialog(),
    IInfoDialogViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: IInfoDialogViewMvc
    private lateinit var title: String
    private lateinit var message: String
    private lateinit var buttonCaption: String

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

        requireArguments().let {
            title = it.getString(ARG_TITLE)!!
            message = it.getString(ARG_MESSAGE)!!
            buttonCaption = it.getString(ARG_BUTTON_CAPTION)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewMvc = viewMvcFactory.newInfoDialogViewMvc(null)

        val dialog = Dialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

        viewMvc.setTitle(title)
        viewMvc.setMessage(message)
        viewMvc.setButtonCaption(buttonCaption)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onButtonClicked() {
        dismiss()
    }
}