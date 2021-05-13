package com.ivettevaldez.saturnus.screens.common.navigation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface INavDrawerViewMvc : IObservableViewMvc<INavDrawerViewMvc.Listener>,
    INavDrawerHelper {

    interface Listener {

        fun onInvoicingClicked()
    }

    fun getFragmentFrame(): FrameLayout
}

class NavDrawerViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<INavDrawerViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_drawer
), INavDrawerViewMvc {

    private val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    private val frameContent: FrameLayout = findViewById(R.id.drawer_frame_content)
    private val navView: NavigationView = findViewById(R.id.drawer_navigation_view)

    init {

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
        navView.setNavigationItemSelectedListener { item ->
            closeDrawer()

            if (item.itemId == R.id.drawer_menu_invoicing) {
                for (listener in listeners) {
                    listener.onInvoicingClicked()
                }
            }

            false
        }
    }
}