package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.app.Dialog

class InfoController : IInfoViewMvc.Listener {

    lateinit var viewMvc: IInfoViewMvc
    lateinit var dialog: Dialog

    lateinit var title: String
    lateinit var message: String
    lateinit var positiveButtonCaption: String

    fun bindDialogAndView(dialog: Dialog, viewMvc: IInfoViewMvc) {
        this.viewMvc = viewMvc
        this.dialog = dialog

        dialog.setContentView(viewMvc.getRootView())
    }

    fun bindArguments(title: String, message: String, positiveButtonCaption: String) {
        this.title = title
        this.message = message
        this.positiveButtonCaption = positiveButtonCaption
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
    }

    private fun bindData() {
        viewMvc.setTitle(title)
        viewMvc.setMessage(message)
        viewMvc.setPositiveButtonCaption(positiveButtonCaption)
    }
}