package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

import android.app.Dialog
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus

class PromptBottomSheetController(
    private val dialogsEventBus: DialogsEventBus
) : IPromptBottomSheetViewMvc.Listener {

    lateinit var dialog: Dialog
    lateinit var viewMvc: IPromptBottomSheetViewMvc

    lateinit var title: String
    lateinit var optionOneCaption: String
    lateinit var optionTwoCaption: String

    fun bindArguments(title: String, optionOneCaption: String, optionTwoCaption: String) {
        this.title = title
        this.optionOneCaption = optionOneCaption
        this.optionTwoCaption = optionTwoCaption
    }

    fun bindDialogAndView(dialog: Dialog, viewMvc: IPromptBottomSheetViewMvc) {
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

    override fun onOptionOneClicked() {
        dialog.dismiss()

        dialogsEventBus.postEvent(
            PromptBottomSheetDialogEvent(PromptBottomSheetDialogEvent.Button.OPTION_ONE)
        )
    }

    override fun onOptionTwoClicked() {
        dialog.dismiss()

        dialogsEventBus.postEvent(
            PromptBottomSheetDialogEvent(PromptBottomSheetDialogEvent.Button.OPTION_TWO)
        )
    }

    private fun bindData() {
        viewMvc.setTitle(title)
        viewMvc.setOptionOneCaption(optionOneCaption)
        viewMvc.setOptionTwoCaption(optionTwoCaption)
    }
}