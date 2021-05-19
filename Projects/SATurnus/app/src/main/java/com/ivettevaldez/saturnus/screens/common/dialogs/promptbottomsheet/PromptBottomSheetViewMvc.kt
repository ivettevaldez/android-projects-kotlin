package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IPromptBottomSheetViewMvc : IObservableViewMvc<IPromptBottomSheetViewMvc.Listener> {

    interface Listener {

        fun onOptionOneClicked()
        fun onOptionTwoClicked()
    }

    fun setTitle(title: String)
    fun setOptionOneCaption(caption: String)
    fun setOptionTwoCaption(caption: String)
}

class PromptBottomSheetViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPromptBottomSheetViewMvc.Listener>(
    inflater,
    parent,
    R.layout.dialog_prompt_bottom_sheet
), IPromptBottomSheetViewMvc {

    private val textTitle: TextView = findViewById(R.id.prompt_bottom_sheet_text_title)
    private val buttonOptionOne: TextView = findViewById(R.id.prompt_bottom_sheet_button_option_one)
    private val buttonOptionTwo: TextView = findViewById(R.id.prompt_bottom_sheet_button_option_two)

    init {

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun setOptionOneCaption(caption: String) {
        buttonOptionOne.text = caption
    }

    override fun setOptionTwoCaption(caption: String) {
        buttonOptionTwo.text = caption
    }

    private fun setListenerEvents() {
        buttonOptionOne.setOnClickListener {
            for (listener in listeners) {
                listener.onOptionOneClicked()
            }
        }

        buttonOptionTwo.setOnClickListener {
            for (listener in listeners) {
                listener.onOptionTwoClicked()
            }
        }
    }
}