package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PromptBottomSheetDialog : BaseBottomSheetDialog(),
    IPromptBottomSheetViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    private lateinit var viewMvc: IPromptBottomSheetViewMvc

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_OPTION_ONE_CAPTION = "ARG_OPTION_ONE_CAPTION"
        private const val ARG_OPTION_TWO_CAPTION = "ARG_OPTION_TWO_CAPTION"

        @JvmStatic
        fun newPromptBottomSheetDialog(
            title: String,
            optionOneCaption: String,
            optionTwoCaption: String
        ): PromptBottomSheetDialog {
            return PromptBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_OPTION_ONE_CAPTION, optionOneCaption)
                    putString(ARG_OPTION_TWO_CAPTION, optionTwoCaption)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        viewMvc = viewMvcFactory.newPromptBottomSheetDialogViewMvc(null)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments == null) {
            throw IllegalArgumentException("@@@@@ Arguments must not be null")
        }

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        viewMvc.setTitle(getTitle())
        viewMvc.setOptionOneCaption(getOptionOneCaption())
        viewMvc.setOptionTwoCaption(getOptionTwoCaption())

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

    override fun onOptionOneClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptBottomSheetDialogEvent(PromptBottomSheetDialogEvent.Button.OPTION_ONE)
        )
    }

    override fun onOptionTwoClicked() {
        dismiss()

        dialogsEventBus.postEvent(
            PromptBottomSheetDialogEvent(PromptBottomSheetDialogEvent.Button.OPTION_TWO)
        )
    }

    private fun getTitle(): String = arguments!!.getString(ARG_TITLE) ?: ""

    private fun getOptionOneCaption(): String = arguments!!.getString(ARG_OPTION_ONE_CAPTION) ?: ""

    private fun getOptionTwoCaption(): String = arguments!!.getString(ARG_OPTION_TWO_CAPTION) ?: ""
}