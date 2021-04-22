package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.infodialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.BaseDialog
import kotlinx.android.synthetic.main.dialog_info.*

private const val ARG_TITLE: String = "ARG_TITLE"
private const val ARG_MESSAGE: String = "ARG_MESSAGE"
private const val ARG_BUTTON_CAPTION: String = "ARG_BUTTON_CAPTION"

class InfoDialog private constructor() : BaseDialog() {

    private lateinit var textTitle: TextView
    private lateinit var textMessage: TextView
    private lateinit var buttonPossitive: Button

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments == null) {
            throw IllegalArgumentException("Arguments must not be null")
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_info)

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

        textTitle = dialog.dialog_text_title
        textMessage = dialog.dialog_text_message
        buttonPossitive = dialog.dialog_button_positive

        textTitle.text = arguments!!.getString(ARG_TITLE)
        textMessage.text = arguments!!.getString(ARG_MESSAGE)
        buttonPossitive.text = arguments!!.getString(ARG_BUTTON_CAPTION)

        buttonPossitive.setOnClickListener {
            onButtonClicked()
        }

        return dialog
    }

    private fun onButtonClicked() {
        dismiss()
    }
}