package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.BaseDialog
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus

private const val ARG_TITLE: String = "ARG_TITLE"
private const val ARG_MESSAGE: String = "ARG_MESSAGE"
private const val ARG_BUTTON_POSITIVE_CAPTION: String = "ARG_BUTTON_POSITIVE_CAPTION"
private const val ARG_BUTTON_NEGATIVE_CAPTION: String = "ARG_BUTTON_NEGATIVE_CAPTION"

class PromptDialog : BaseDialog(),
    IPromptViewMvc.Listener {

    private lateinit var dialogsEventBus: DialogsEventBus
    private lateinit var viewMvc: IPromptViewMvc

    companion object {

        @JvmStatic
        fun newPromptDialog(
            title: String,
            message: String,
            buttonPositiveCaption: String,
            buttonNegativeCaption: String
        ): PromptDialog {
            return PromptDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_BUTTON_POSITIVE_CAPTION, buttonPositiveCaption)
                    putString(ARG_BUTTON_NEGATIVE_CAPTION, buttonNegativeCaption)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
        viewMvc = getCompositionRoot().getViewMvcFactory().getPromptViewMvc(null)
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
        viewMvc.setNegativeButtonCaption(getNegativeButtonCaption())

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

    override fun onPositiveButtonClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.POSITIVE)
        )
    }

    override fun onNegativeButtonClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE)
        )
    }

    private fun getTitle(): String = arguments!!.getString(ARG_TITLE) ?: ""

    private fun getMessage(): String = arguments!!.getString(ARG_MESSAGE) ?: ""

    private fun getPositiveButtonCaption(): String =
        arguments!!.getString(ARG_BUTTON_POSITIVE_CAPTION) ?: ""

    private fun getNegativeButtonCaption(): String =
        arguments!!.getString(ARG_BUTTON_NEGATIVE_CAPTION) ?: ""
}