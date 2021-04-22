package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.BaseDialog
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus
import kotlinx.android.synthetic.main.dialog_prompt.*

private const val ARG_TITLE: String = "ARG_TITLE"
private const val ARG_MESSAGE: String = "ARG_MESSAGE"
private const val ARG_BUTTON_POSITIVE_CAPTION: String = "ARG_BUTTON_POSITIVE_CAPTION"
private const val ARG_BUTTON_NEGATIVE_CAPTION: String = "ARG_BUTTON_NEGATIVE_CAPTION"

class PromptDialog : BaseDialog() {

    private lateinit var dialogsEventBus: DialogsEventBus

    private lateinit var textTitle: TextView
    private lateinit var textMessage: TextView
    private lateinit var buttonPositive: Button
    private lateinit var buttonNegative: Button

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
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments == null) {
            throw IllegalArgumentException("Arguments must not be null")
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_prompt)

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

        textTitle = dialog.prompt_text_title
        textMessage = dialog.prompt_text_message
        buttonPositive = dialog.prompt_button_positive
        buttonNegative = dialog.prompt_button_negative

        textTitle.text = arguments!!.getString(ARG_TITLE)
        textMessage.text = arguments!!.getString(ARG_MESSAGE)
        buttonPositive.text = arguments!!.getString(ARG_BUTTON_POSITIVE_CAPTION)
        buttonNegative.text = arguments!!.getString(ARG_BUTTON_NEGATIVE_CAPTION)

        buttonPositive.setOnClickListener {
            onPositiveButtonClicked()
        }

        buttonNegative.setOnClickListener {
            onNegativeButtonClicked()
        }

        return dialog
    }

    private fun onPositiveButtonClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.POSITIVE)
        )
    }

    private fun onNegativeButtonClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE)
        )
    }
}