package com.ivettevaldez.saturnus.screens.common.main

import android.widget.FrameLayout
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import javax.inject.Inject

class MainController @Inject constructor(
    private val screensNavigator: ScreensNavigator
) : INavDrawerViewMvc.Listener {

    private lateinit var viewMvc: INavDrawerViewMvc

    fun bindView(viewMvc: INavDrawerViewMvc) {
        this.viewMvc = viewMvc
    }

    fun bindCopyright(copyright: String) {
        viewMvc.bindCopyright(copyright)
    }

    fun toSplash() {
        screensNavigator.toSplash()
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    fun isDrawerOpen(): Boolean = viewMvc.isDrawerOpen()

    fun openDrawer() {
        viewMvc.openDrawer()
    }

    fun closeDrawer() {
        viewMvc.closeDrawer()
    }

    fun getFragmentFrame(): FrameLayout = viewMvc.getFragmentFrame()

    override fun onPeopleClicked() {
        screensNavigator.toPeople()
    }

    override fun onInvoicingClicked() {
        screensNavigator.toInvoiceIssuingPeople()
    }
}