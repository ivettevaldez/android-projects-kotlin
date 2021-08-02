package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog

class InfoDialogController : IInfoDialogViewMvc.Listener {

    lateinit var viewMvc: IInfoDialogViewMvc
    lateinit var dialog: Dialog

    fun bindDialogAndView(dialog: Dialog, viewMvc: IInfoDialogViewMvc) {
        this.viewMvc = viewMvc
        this.dialog = dialog

        dialog.setContentView(viewMvc.getRootView())
    }

    fun bindData(title: String, message: String, buttonCaption: String) {
        viewMvc.setTitle(title)
        viewMvc.setMessage(message)
        viewMvc.setButtonCaption(buttonCaption)
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onButtonClicked() {
        dialog.dismiss()
    }
}