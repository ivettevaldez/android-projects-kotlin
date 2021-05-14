package com.ivettevaldez.saturnus.screens.common.navigation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface INavDrawerViewMvc : IObservableViewMvc<INavDrawerViewMvc.Listener>,
    INavDrawerHelper {

    interface Listener {

        fun onPeopleClicked()
        fun onInvoicingClicked()
    }

    fun getFragmentFrame(): FrameLayout
    fun setCopyright(copyright: String)
}

class NavDrawerViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<INavDrawerViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_nav_drawer
), INavDrawerViewMvc {

    private val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    private val frameContent: FrameLayout = findViewById(R.id.drawer_frame_content)
    private val navView: NavigationView = findViewById(R.id.drawer_navigation_view)
    private val textCopyright: TextView = findViewById(R.id.drawer_text_copyright)

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

    override fun setCopyright(copyright: String) {
        textCopyright.text = copyright
    }

    private fun setListenerEvents() {
        navView.setNavigationItemSelectedListener { item ->
            closeDrawer()

            if (item.itemId == R.id.drawer_menu_invoicing) {
                for (listener in listeners) {
                    listener.onInvoicingClicked()
                }
            } else if (item.itemId == R.id.drawer_menu_people) {
                for (listener in listeners) {
                    listener.onPeopleClicked()
                }
            }

            false
        }
    }
}