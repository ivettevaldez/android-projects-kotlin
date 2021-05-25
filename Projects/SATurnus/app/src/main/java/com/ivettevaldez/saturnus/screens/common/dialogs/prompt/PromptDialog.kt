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
    private lateinit var title: String
    private lateinit var message: String
    private lateinit var positiveCaption: String
    private lateinit var negativeCaption: String

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_CAPTION = "ARG_POSITIVE_CAPTION"
        private const val ARG_NEGATIVE_CAPTION = "ARG_NEGATIVE_CAPTION"

        fun newPromptDialog(
            title: String,
            message: String,
            positiveCaption: String,
            negativeCaption: String
        ): PromptDialog =
            PromptDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putString(ARG_POSITIVE_CAPTION, positiveCaption)
                    putString(ARG_NEGATIVE_CAPTION, negativeCaption)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            title = it.getString(ARG_TITLE)!!
            message = it.getString(ARG_MESSAGE)!!
            positiveCaption = it.getString(ARG_POSITIVE_CAPTION)!!
            negativeCaption = it.getString(ARG_NEGATIVE_CAPTION)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewMvc = viewMvcFactory.newPromptDialogViewMvc(null)

        val dialog = Dialog(requireContext())
        dialog.setContentView(viewMvc.getRootView())

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)

        viewMvc.setTitle(title)
        viewMvc.setMessage(message)
        viewMvc.setPositiveCaption(positiveCaption)
        viewMvc.setNegativeCaption(negativeCaption)

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
}