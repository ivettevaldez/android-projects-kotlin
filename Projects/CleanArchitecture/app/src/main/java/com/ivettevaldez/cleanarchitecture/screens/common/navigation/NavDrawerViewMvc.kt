package com.ivettevaldez.cleanarchitecture.screens.common.navigation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.views.IObservableViewMvc
import kotlinx.android.synthetic.main.layout_drawer.view.*

interface INavDrawerViewMvc : IObservableViewMvc<INavDrawerViewMvc.Listener> {

    interface Listener {

        fun onQuestionsListClicked()
    }

    fun getFragmentFrame(): FrameLayout
    fun isDrawerOpen(): Boolean
    fun openDrawer()
    fun closeDrawer()
}

class NavDrawerViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<INavDrawerViewMvc.Listener>(),
    INavDrawerViewMvc {

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

    override fun getFragmentFrame(): FrameLayout = frameContent

    override fun isDrawerOpen(): Boolean = drawerLayout.isDrawerOpen(Gravity.START)

    override fun openDrawer() {
        drawerLayout.openDrawer(Gravity.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawers()
    }

    private fun setListenerEvents() {
        navigationView.setNavigationItemSelectedListener { item ->
            closeDrawer()

            if (item.itemId == R.id.drawer_menu_questions_list) {
                for (listener in getListeners()) {
                    listener.onQuestionsListClicked()
                }
            }

            false
        }
    }
}