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

    interface MenuClickListener {
        fun onMenuClicked()
    }

    interface NavigateUpClickListener {
        fun onNavigateUpClicked()
    }

    fun setTitle(title: String)
    fun enableMenuAndListen(listener: MenuClickListener)
    fun enableUpNavigationAndListen(listener: NavigateUpClickListener)
}

class ToolbarViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(),
    IToolbarViewMvc {

    private val textTitle: TextView
    private val buttonMenu: ImageButton
    private val buttonNavigateUp: ImageButton

    private var menuClickListener: IToolbarViewMvc.MenuClickListener? = null
    private var navigateUpClickListener: IToolbarViewMvc.NavigateUpClickListener? = null

    init {

        setRootView(
            inflater.inflate(R.layout.layout_toolbar, parent, false)
        )

        textTitle = getRootView().toolbar_text_title
        buttonMenu = getRootView().toolbar_button_menu
        buttonNavigateUp = getRootView().toolbar_button_navigate_up

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun enableMenuAndListen(listener: IToolbarViewMvc.MenuClickListener) {
        if (navigateUpClickListener != null) {
            throw Exception("Menu and Navigate Up shouldn't be shown together")
        }
        menuClickListener = listener
        buttonMenu.visibility = View.VISIBLE
    }

    override fun enableUpNavigationAndListen(listener: IToolbarViewMvc.NavigateUpClickListener) {
        if (menuClickListener != null) {
            throw Exception("Menu and Navigate Up shouldn't be shown together")
        }
        navigateUpClickListener = listener
        buttonNavigateUp.visibility = View.VISIBLE
    }

    private fun setListenerEvents() {
        buttonMenu.setOnClickListener {
            menuClickListener!!.onMenuClicked()
        }

        buttonNavigateUp.setOnClickListener {
            navigateUpClickListener!!.onNavigateUpClicked()
        }
    }
}