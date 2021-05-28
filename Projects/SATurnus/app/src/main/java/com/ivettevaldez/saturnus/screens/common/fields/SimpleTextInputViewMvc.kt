package com.ivettevaldez.saturnus.screens.common.fields

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface ISimpleTextInputViewMvc : IViewMvc {

    interface ClickListener {

        fun onEditTextClicked()
    }

    fun enable()
    fun disable()
    fun clean()
    fun getText(): String
    fun setText(text: String)
    fun setHint(hint: String)
    fun setDrawable(@DrawableRes drawable: Int)
    fun setInputType(type: Int)
    fun setImeOptions(options: Int)
    fun enableClickAndListen(listener: ClickListener)
    fun addTextChangedListener(watcher: TextWatcher)
}

class SimpleTextInputViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.element_input_simple_text
), ISimpleTextInputViewMvc {

    private val inputLayout: TextInputLayout = findViewById(R.id.text_input_layout_simple)
    private val editText: TextInputEditText = findViewById(R.id.text_input_edit_text_simple)

    private var clickListener: ISimpleTextInputViewMvc.ClickListener? = null

    init {

        setListenerEvents()
    }

    override fun enable() {
        editText.isEnabled = true
    }

    override fun disable() {
        editText.isFocusable = false
    }

    override fun clean() {
        setText("")
    }

    override fun getText(): String = editText.text.toString().trim()

    override fun setText(text: String) {
        editText.setText(text)
    }

    override fun setHint(hint: String) {
        inputLayout.hint = hint
    }

    override fun setDrawable(drawable: Int) {
        editText.setCompoundDrawablesWithIntrinsicBounds(
            drawable, 0, 0, 0
        )
    }

    override fun setInputType(type: Int) {
        editText.inputType = type
    }

    override fun setImeOptions(options: Int) {
        editText.imeOptions = options
    }

    override fun enableClickAndListen(listener: ISimpleTextInputViewMvc.ClickListener) {
        clickListener = listener
    }

    override fun addTextChangedListener(watcher: TextWatcher) {
        editText.addTextChangedListener(watcher)
    }

    private fun setListenerEvents() {
        editText.setOnClickListener {
            clickListener?.onEditTextClicked()
        }
    }
}