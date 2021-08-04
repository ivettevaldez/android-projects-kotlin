package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

import android.app.Dialog
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus

class PromptController(
    private val dialogsEventBus: DialogsEventBus
) : IPromptViewMvc.Listener {

    lateinit var dialog: Dialog
    lateinit var viewMvc: IPromptViewMvc

    lateinit var title: String
    lateinit var message: String
    lateinit var positiveCaption: String
    lateinit var negativeCaption: String

    fun bindArguments(
        title: String,
        message: String,
        positiveCaption: String,
        negativeCaption: String
    ) {
        this.title = title
        this.message = message
        this.positiveCaption = positiveCaption
        this.negativeCaption = negativeCaption
    }

    fun bindDialogAndView(dialog: Dialog, viewMvc: IPromptViewMvc) {
        this.dialog = dialog
        this.viewMvc = viewMvc

        dialog.setContentView(viewMvc.getRootView())
    }

    fun onStart() {
        viewMvc.registerListener(this)

        bindData()
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onPositiveButtonClicked() {
        dialog.dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.POSITIVE)
        )
    }

    override fun onNegativeButtonClicked() {
        dialog.dismiss()

        dialogsEventBus.postEvent(
            PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE)
        )
    }

    private fun bindData() {
        viewMvc.setTitle(title)
        viewMvc.setMessage(message)
        viewMvc.setPositiveCaption(positiveCaption)
        viewMvc.setNegativeCaption(negativeCaption)
    }
}