package com.ivettevaldez.saturnus.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity
import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class MainActivity : BaseActivity(),
    IFragmentFrameWrapper,
    INavDrawerViewMvc.Listener,
    INavDrawerHelper {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    private lateinit var viewMvc: INavDrawerViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newNavDrawerViewMvc(null)
        setContentView(viewMvc.getRootView())

        if (savedInstanceState == null) {
            screensNavigator.toInvoicing()
        }
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onBackPressed() {
        if (viewMvc.isDrawerOpen()) {
            viewMvc.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun getFragmentFrame(): FrameLayout = viewMvc.getFragmentFrame()

    override fun isDrawerOpen(): Boolean = viewMvc.isDrawerOpen()

    override fun openDrawer() {
        viewMvc.openDrawer()
    }

    override fun closeDrawer() {
        viewMvc.closeDrawer()
    }

    override fun onInvoicingClicked() {
        screensNavigator.toInvoicing()
    }
}