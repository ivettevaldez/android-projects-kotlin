package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.infodialog

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.BaseDialog

private const val ARG_TITLE: String = "ARG_TITLE"
private const val ARG_MESSAGE: String = "ARG_MESSAGE"
private const val ARG_BUTTON_CAPTION: String = "ARG_BUTTON_CAPTION"

class InfoDialog : BaseDialog(),
    IInfoViewMvc.Listener {

    private lateinit var viewMvc: IInfoViewMvc

    companion object {

        @JvmStatic
        fun newInfoDialog(title: String, message: String, buttonCaption: String): InfoDialog {
            return InfoDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_BUTTON_CAPTION, buttonCaption)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMvc = getCompositionRoot().getViewMvcFactory().getInfoViewMvc(null)
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
        viewMvc.setPositiveButtonCaption(getPositiveButtonCaption())

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

    private fun getPositiveButtonCaption(): String = arguments!!.getString(ARG_BUTTON_CAPTION) ?: ""
}