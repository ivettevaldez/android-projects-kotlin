package com.ivettevaldez.cleanarchitecture.screens.common.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IViewMvc
import kotlinx.android.synthetic.main.layout_toolbar.view.*

interface IToolbarViewMvc : IViewMvc {

    fun setTitle(title: String)
    fun enableMenuAndListen(listener: View.OnClickListener)
    fun enableUpNavigationAndListen(listener: View.OnClickListener)
}

class ToolbarViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(),
    IToolbarViewMvc {

    private val textTitle: TextView
    private val buttonMenu: ImageButton
    private val buttonNavigateUp: ImageButton

    init {

        setRootView(
            inflater.inflate(R.layout.layout_toolbar, parent, false)
        )

        textTitle = getRootView().toolbar_text_title
        buttonMenu = getRootView().toolbar_button_menu
        buttonNavigateUp = getRootView().toolbar_button_back
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun enableMenuAndListen(listener: View.OnClickListener) {
        buttonMenu.visibility = View.VISIBLE
        buttonMenu.setOnClickListener(listener)
    }

    override fun enableUpNavigationAndListen(listener: View.OnClickListener) {
        buttonNavigateUp.visibility = View.VISIBLE
        buttonNavigateUp.setOnClickListener(listener)
    }
}