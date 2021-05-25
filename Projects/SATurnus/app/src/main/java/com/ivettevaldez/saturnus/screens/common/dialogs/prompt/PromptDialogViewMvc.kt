package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IPromptDialogViewMvc : IObservableViewMvc<IPromptDialogViewMvc.Listener> {

    interface Listener {

        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
    }

    fun setTitle(title: String)
    fun setMessage(message: String)
    fun setPositiveCaption(caption: String)
    fun setNegativeCaption(caption: String)
}

class PromptDialogViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPromptDialogViewMvc.Listener>(
    inflater,
    parent,
    R.layout.dialog_prompt
), IPromptDialogViewMvc {

    private val textTitle: TextView = findViewById(R.id.prompt_text_title)
    private val textMessage: TextView = findViewById(R.id.prompt_text_message)
    private val buttonPositive: Button = findViewById(R.id.prompt_button_positive)
    private val buttonNegative: Button = findViewById(R.id.prompt_button_negative)

    init {

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun setMessage(message: String) {
        textMessage.text = message
    }

    override fun setPositiveCaption(caption: String) {
        buttonPositive.text = caption
    }

    override fun setNegativeCaption(caption: String) {
        buttonNegative.text = caption
    }

    private fun setListenerEvents() {
        buttonPositive.setOnClickListener {
            for (listener in listeners) {
                listener.onPositiveButtonClicked()
            }
        }

        buttonNegative.setOnClickListener {
            for (listener in listeners) {
                listener.onNegativeButtonClicked()
            }
        }
    }
}