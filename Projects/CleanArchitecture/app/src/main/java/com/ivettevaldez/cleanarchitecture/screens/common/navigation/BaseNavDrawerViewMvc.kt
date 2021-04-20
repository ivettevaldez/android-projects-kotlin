package com.ivettevaldez.cleanarchitecture.screens.common.navigation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseObservableViewMvc
import kotlinx.android.synthetic.main.layout_drawer.view.*

abstract class BaseNavDrawerViewMvc<ListenerType>(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<ListenerType>() {

    private val drawerLayout: DrawerLayout
    private val frameContent: FrameLayout
    private val navigationView: NavigationView

    init {

        super.setRootView(
            inflater.inflate(R.layout.layout_drawer, parent, false)
        )

        drawerLayout = getRootView().drawer_layout
        frameContent = getRootView().drawer_frame_container
        navigationView = getRootView().drawer_navigation_view

        setListenerEvents()
    }

    abstract fun onDrawerItemClicked(item: DrawerItems)

    override fun setRootView(view: View) {
        frameContent.addView(view)
    }

    protected fun openDrawer() {
        drawerLayout.openDrawer(Gravity.START)
    }

    private fun setListenerEvents() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()

            if (item.itemId == R.id.drawer_menu_questions_list) {
                onDrawerItemClicked(DrawerItems.QUESTIONS_LIST)
            }

            false
        }
    }
}