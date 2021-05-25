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
    private lateinit var title: String
    private lateinit var optionOneCaption: String
    private lateinit var optionTwoCaption: String

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_OPTION_ONE_CAPTION = "ARG_OPTION_ONE_CAPTION"
        private const val ARG_OPTION_TWO_CAPTION = "ARG_OPTION_TWO_CAPTION"

        @JvmStatic
        fun newPromptBottomSheetDialog(
            title: String,
            optionOneCaption: String,
            optionTwoCaption: String
        ): PromptBottomSheetDialog =
            PromptBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_OPTION_ONE_CAPTION, optionOneCaption)
                    putString(ARG_OPTION_TWO_CAPTION, optionTwoCaption)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            title = it.getString(ARG_TITLE)!!
            optionOneCaption = it.getString(ARG_OPTION_ONE_CAPTION)!!
            optionTwoCaption = it.getString(ARG_OPTION_TWO_CAPTION)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewMvc = viewMvcFactory.newPromptBottomSheetDialogViewMvc(null)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        viewMvc.setTitle(title)
        viewMvc.setOptionOneCaption(optionOneCaption)
        viewMvc.setOptionTwoCaption(optionTwoCaption)

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
}