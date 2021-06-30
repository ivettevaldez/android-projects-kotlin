package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ViewMvc

interface ToolbarViewMvc : ViewMvc {

    interface NavigateUpListener {

        fun onNavigateUpClicked()
    }

    interface MenuListener {

        fun onMenuClicked()
    }

    fun setTitle(title: String)
    fun enableNavigateUpAndListen(listener: NavigateUpListener)
    fun enableMenuAndListen(listener: MenuListener)
}

class ToolbarViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(),
    ToolbarViewMvc {

    private val textTitle: TextView
    private val buttonNavigateUp: ImageButton
    private val buttonMenu: ImageButton

    private var navigateUpListener: ToolbarViewMvc.NavigateUpListener? = null
    private var menuListener: ToolbarViewMvc.MenuListener? = null

    init {

        setRootView(
            inflater.inflate(R.layout.layout_toolbar, parent, false)
        )

        textTitle = getRootView()!!.findViewById(R.id.toolbar_text_title)
        buttonNavigateUp = getRootView()!!.findViewById(R.id.toolbar_button_navigate_up)
        buttonMenu = getRootView()!!.findViewById(R.id.toolbar_button_menu)

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun enableNavigateUpAndListen(listener: ToolbarViewMvc.NavigateUpListener) {
        if (menuListener != null) {
            throw RuntimeException("Menu and navigate up buttons cannot be shown together")
        }
        navigateUpListener = listener
        buttonNavigateUp.visibility = View.VISIBLE
    }

    override fun enableMenuAndListen(listener: ToolbarViewMvc.MenuListener) {
        if (navigateUpListener != null) {
            throw RuntimeException("Menu and navigate up buttons cannot be shown together")
        }
        menuListener = listener
        buttonMenu.visibility = View.VISIBLE
    }

    private fun setListenerEvents() {
        buttonNavigateUp.setOnClickListener {
            navigateUpListener!!.onNavigateUpClicked()
        }

        buttonMenu.setOnClickListener {
            menuListener!!.onMenuClicked()
        }
    }
}