package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseObservableViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ObservableViewMvc

interface NavDrawerViewMvc : ObservableViewMvc<NavDrawerViewMvc.Listener>,
    NavDrawerHelper {

    interface Listener {

        fun onQuestionsListClicked()
    }

    fun getFragmentFrame(): FrameLayout
}

class NavDrawerViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<NavDrawerViewMvc.Listener>(),
    NavDrawerViewMvc {

    private val drawerLayout: DrawerLayout
    private val frameContainer: FrameLayout

    init {

        setRootView(
            inflater.inflate(R.layout.layout_drawer, parent, false)
        )

        drawerLayout = getRootView()!!.findViewById(R.id.drawer_layout)
        frameContainer = getRootView()!!.findViewById(R.id.frame_content)
    }

    override fun getFragmentFrame(): FrameLayout = frameContainer

    override fun isDrawerOpen(): Boolean = drawerLayout.isDrawerOpen(Gravity.START)

    override fun openDrawer() {
        drawerLayout.openDrawer(Gravity.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawers()
    }
}