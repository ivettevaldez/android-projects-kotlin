package com.ivettevaldez.saturnus.screens.common.fields

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.ArrayRes
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface ISpinnerInputViewMvc : IViewMvc {

    fun enable()
    fun disable()
    fun clean()
    fun getText(): String
    fun setSelection(position: Int)
    fun setHint(hint: String)
    fun bindValues(@ArrayRes arrayOfValues: Int)
}

class SpinnerInputViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.element_input_spinner
), ISpinnerInputViewMvc {

    private val spinner: Spinner = findViewById(R.id.text_input_spinner)
    private val textLabel: TextView = findViewById(R.id.text_input_spinner_label)

    companion object {

        private const val PLACEHOLDER_POSITION = 0
    }

    override fun enable() {
        spinner.isEnabled = true
    }

    override fun disable() {
        spinner.isEnabled = false
    }

    override fun clean() {
        setSelection(0)
    }

    override fun getText(): String {
        return if (spinner.selectedItemPosition == PLACEHOLDER_POSITION) {
            ""
        } else {
            spinner.selectedItem.toString().trim()
        }
    }

    override fun setSelection(position: Int) {
        spinner.setSelection(position)
    }

    override fun setHint(hint: String) {
        textLabel.text = hint
    }

    override fun bindValues(arrayOfValues: Int) {
        spinner.adapter = ArrayAdapter.createFromResource(
            context,
            arrayOfValues,
            R.layout.item_spinner
        )
    }
}