package com.ivettevaldez.saturnus.screens.common.dialogs.info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IInfoViewMvc : IObservableViewMvc<IInfoViewMvc.Listener> {

    interface Listener {

        fun onPositiveButtonClicked()
    }

    fun setTitle(title: String)
    fun setMessage(message: String)
    fun setPositiveButtonCaption(caption: String)
}

class InfoViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IInfoViewMvc.Listener>(
    inflater,
    parent,
    R.layout.dialog_info
), IInfoViewMvc {

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

    override fun setPositiveButtonCaption(caption: String) {
        buttonPositive.text = caption
    }

    private fun setListenerEvents() {
        buttonPositive.setOnClickListener {
            for (listener in listeners) {
                listener.onPositiveButtonClicked()
            }
        }
    }
}