package com.ivettevaldez.cleanarchitecture.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseViewMvc
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class ToolbarViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc() {

    private val textTitle: TextView
    private val buttonMenu: ImageButton
    private val buttonBack: ImageButton

    init {

        setRootView(
            inflater.inflate(R.layout.layout_toolbar, parent, false)
        )

        textTitle = getRootView().toolbar_text_title
        buttonMenu = getRootView().toolbar_button_menu
        buttonBack = getRootView().toolbar_button_back
    }

    fun setTitle(title: String) {
        textTitle.text = title
    }
}