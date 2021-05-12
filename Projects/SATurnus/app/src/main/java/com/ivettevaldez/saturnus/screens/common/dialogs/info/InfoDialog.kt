package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_MESSAGE = "ARG_MESSAGE"
private const val ARG_BUTTON_CAPTION = "ARG_BUTTON_CAPTION"

class InfoDialog : BaseDialog(),
    IInfoDialogViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: IInfoDialogViewMvc

    companion object {

        @JvmStatic
        fun newInfoDialog(title: String, message: String, caption: String): InfoDialog {
            return InfoDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_BUTTON_CAPTION, caption)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        viewMvc = viewMvcFactory.newInfoDialogViewMvc(null)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments == null) {
            throw IllegalArgumentException("Arguments must not be null")
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

        viewMvc.setTitle(getTitle())
        viewMvc.setMessage(getMessage())
        viewMvc.setButtonCaption(getButtonCaption())

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

    private fun getTitle(): String = arguments!!.getString(ARG_TITLE) ?: ""

    private fun getMessage(): String = arguments!!.getString(ARG_MESSAGE) ?: ""

    private fun getButtonCaption(): String = arguments!!.getString(ARG_BUTTON_CAPTION) ?: ""
}