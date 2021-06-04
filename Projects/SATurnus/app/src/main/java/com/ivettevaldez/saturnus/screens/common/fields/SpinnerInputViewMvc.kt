package com.ivettevaldez.saturnus.screens.common.fields

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.ArrayRes
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface ISpinnerInputViewMvc : IViewMvc {

    interface ItemSelectedListener {

        fun onItemSelected()
    }

    fun enable()
    fun disable()
    fun clean()
    fun getText(): String
    fun setSelection(position: Int)
    fun setHint(hint: String)
    fun bindValues(@ArrayRes arrayOfValues: Int)
    fun addItemSelectedListener(listener: ItemSelectedListener)
}

class SpinnerInputViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.element_input_spinner
), ISpinnerInputViewMvc {

    private var itemSelectedListener: ISpinnerInputViewMvc.ItemSelectedListener? = null

    private val spinner: Spinner = findViewById(R.id.text_input_spinner)
    private val textLabel: TextView = findViewById(R.id.text_input_spinner_label)

    companion object {

        private const val PLACEHOLDER_POSITION = 0
    }

    init {

        setListenerEvents()
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
        return if (spinner.selectedItemPosition != PLACEHOLDER_POSITION) {
            spinner.selectedItem.toString().trim()
        } else {
            ""
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

    override fun addItemSelectedListener(listener: ISpinnerInputViewMvc.ItemSelectedListener) {
        itemSelectedListener = listener
    }

    private fun setListenerEvents() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, item: View?, position: Int, p3: Long) {
                if (position != 0) {
                    itemSelectedListener?.onItemSelected()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing.
            }
        }
    }
}