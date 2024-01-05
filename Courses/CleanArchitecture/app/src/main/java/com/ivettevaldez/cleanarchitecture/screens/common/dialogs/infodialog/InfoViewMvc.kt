package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.infodialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.dialog_info.view.*

interface IInfoViewMvc : IObservableViewMvc<IInfoViewMvc.Listener> {

    interface Listener {

        fun onButtonClicked()
    }

    fun setTitle(title: String)
    fun setMessage(message: String)
    fun setPositiveButtonCaption(caption: String)
}

class InfoViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IInfoViewMvc.Listener>(),
    IInfoViewMvc {

    private val textTitle: TextView
    private val textMessage: TextView
    private val buttonPositive: Button

    init {

        setRootView(
            inflater.inflate(R.layout.dialog_info, parent, false)
        )

        textTitle = getRootView().info_text_title
        textMessage = getRootView().info_text_message
        buttonPositive = getRootView().info_button_positive

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
            for (listener in getListeners()) {
                listener.onButtonClicked()
            }
        }
    }
}