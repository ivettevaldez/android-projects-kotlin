package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

import android.app.Dialog
import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PromptDialog : BaseDialog(),
    IPromptDialogViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    private lateinit var viewMvc: IPromptDialogViewMvc

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION"
        private const val ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION"

        fun newPromptDialog(
            title: String,
            message: String,
            positiveCaption: String,
            negativeCaption: String
        ): PromptDialog {
            return PromptDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_POSITIVE_BUTTON_CAPTION, positiveCaption)
                    putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeCaption)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        viewMvc = viewMvcFactory.newPromptDialogViewMvc(null)

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

    private fun getPositiveButtonCaption(): String {
        return arguments!!.getString(ARG_POSITIVE_BUTTON_CAPTION) ?: ""
    }

    private fun getNegativeButtonCaption(): String {
        return arguments!!.getString(ARG_NEGATIVE_BUTTON_CAPTION) ?: ""
    }
}