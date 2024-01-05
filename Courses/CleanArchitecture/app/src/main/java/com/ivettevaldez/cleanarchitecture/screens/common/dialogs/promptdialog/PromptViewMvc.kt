package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.dialog_prompt.view.*

interface IPromptViewMvc : IObservableViewMvc<IPromptViewMvc.Listener> {

    interface Listener {

        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
    }

    fun setTitle(title: String)
    fun setMessage(message: String)
    fun setPositiveButtonCaption(caption: String)
    fun setNegativeButtonCaption(caption: String)
}

class PromptViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPromptViewMvc.Listener>(),
    IPromptViewMvc {

    private val textTitle: TextView
    private val textMessage: TextView
    private val buttonPositive: Button
    private val buttonNegative: Button

    init {

        setRootView(
            inflater.inflate(R.layout.dialog_prompt, parent, false)
        )

        textTitle = getRootView().prompt_text_title
        textMessage = getRootView().prompt_text_message
        buttonPositive = getRootView().prompt_button_positive
        buttonNegative = getRootView().prompt_button_negative

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun setMessage(message: String) {
        textMessage.text = message
    }

    override fun setPositiveButtonCaption(caption: String) {
        buttonPositive.text = caption
    }

    override fun setNegativeButtonCaption(caption: String) {
        buttonNegative.text = caption
    }

    private fun setListenerEvents() {
        buttonPositive.setOnClickListener {
            for (listener in getListeners()) {
                listener.onPositiveButtonClicked()
            }
        }

        buttonNegative.setOnClickListener {
            for (listener in getListeners()) {
                listener.onNegativeButtonClicked()
            }
        }
    }
}