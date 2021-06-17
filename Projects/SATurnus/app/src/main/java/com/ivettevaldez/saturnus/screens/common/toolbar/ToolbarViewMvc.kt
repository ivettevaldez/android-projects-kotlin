package com.ivettevaldez.saturnus.screens.common.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface IToolbarViewMvc : IViewMvc {

    interface MenuClickListener {

        fun onMenuClicked()
    }

    interface NavigateUpClickListener {

        fun onNavigateUpClicked()
    }

    interface DeleteClickListener {

        fun onDeleteClicked()
    }

    fun setTitle(title: String)
    fun enableMenuAndListen(listener: MenuClickListener)
    fun enableNavigateUpAndListen(listener: NavigateUpClickListener)
    fun enableDeleteAndListen(listener: DeleteClickListener)
}

class ToolbarViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.layout_nav_toolbar
), IToolbarViewMvc {

    private val textTitle: TextView = findViewById(R.id.toolbar_text_title)
    private val buttonMenu: ImageButton = findViewById(R.id.toolbar_button_menu)
    private val buttonNavigateUp: ImageButton = findViewById(R.id.toolbar_button_navigate_up)
    private val buttonDelete: ImageButton = findViewById(R.id.toolbar_button_delete)

    private var menuClickListener: IToolbarViewMvc.MenuClickListener? = null
    private var navigateUpClickListener: IToolbarViewMvc.NavigateUpClickListener? = null
    private var deleteClickListener: IToolbarViewMvc.DeleteClickListener? = null

    init {

        setListenerEvents()
    }

    override fun setTitle(title: String) {
        textTitle.text = title
    }

    override fun enableMenuAndListen(listener: IToolbarViewMvc.MenuClickListener) {
        if (navigateUpClickListener != null) {
            throw RuntimeException("Menu and Navigate Up shouldn't be shown together")
        }
        menuClickListener = listener
        buttonMenu.visibility = View.VISIBLE
    }

    override fun enableNavigateUpAndListen(listener: IToolbarViewMvc.NavigateUpClickListener) {
        if (menuClickListener != null) {
            throw RuntimeException("Menu and Navigate Up shouldn't be shown together")
        }
        navigateUpClickListener = listener
        buttonNavigateUp.visibility = View.VISIBLE
    }

    override fun enableDeleteAndListen(listener: IToolbarViewMvc.DeleteClickListener) {
        deleteClickListener = listener
        buttonDelete.visibility = View.VISIBLE
    }

    private fun setListenerEvents() {
        buttonMenu.setOnClickListener {
            menuClickListener!!.onMenuClicked()
        }

        buttonNavigateUp.setOnClickListener {
            navigateUpClickListener!!.onNavigateUpClicked()
        }

        buttonDelete.setOnClickListener {
            deleteClickListener!!.onDeleteClicked()
        }
    }
}