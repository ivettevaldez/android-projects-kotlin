package com.ivettevaldez.saturnus.screens.common.main

import android.widget.FrameLayout
import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import javax.inject.Inject

class MainController @Inject constructor(
    private val screensNavigator: ScreensNavigator
) : INavDrawerViewMvc.Listener,
    IFragmentFrameWrapper,
    INavDrawerHelper {

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

    fun onBackPressed(): Boolean {
        return if (viewMvc.isDrawerOpen()) {
            viewMvc.closeDrawer()
            false
        } else {
            screensNavigator.navigateUp()
            true
        }
    }

    override fun isDrawerOpen(): Boolean = viewMvc.isDrawerOpen()

    override fun openDrawer() {
        viewMvc.openDrawer()
    }

    override fun closeDrawer() {
        viewMvc.closeDrawer()
    }

    override fun getFragmentFrame(): FrameLayout = viewMvc.getFragmentFrame()

    override fun onPeopleClicked() {
        screensNavigator.toPeople()
    }

    override fun onInvoicingClicked() {
        screensNavigator.toInvoiceIssuingPeople()
    }
}