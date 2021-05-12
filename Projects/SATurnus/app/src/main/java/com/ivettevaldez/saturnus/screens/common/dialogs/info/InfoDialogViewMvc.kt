package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IInfoDialogViewMvc : IObservableViewMvc<IInfoDialogViewMvc.Listener> {

    interface Listener {

        fun onButtonClicked()
    }

    fun setTitle(title: String)
    fun setMessage(message: String)
    fun setButtonCaption(caption: String)
}

class InfoDialogViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IInfoDialogViewMvc.Listener>(
    inflater,
    parent,
    R.layout.dialog_info
), IInfoDialogViewMvc {

    private val textTitle: TextView = findViewById(R.id.info_text_title)
    private val textMessage: TextView = findViewById(R.id.info_text_message)
    private val buttonPositive: Button = findViewById(R.id.info_button_positive)

    init {

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun setMessage(message: String) {
        textMessage.text = message
    }

    override fun setButtonCaption(caption: String) {
        buttonPositive.text = caption
    }

    private fun setListenerEvents() {
        buttonPositive.setOnClickListener {
            for (listener in listeners) {
                listener.onButtonClicked()
            }
        }
    }
}