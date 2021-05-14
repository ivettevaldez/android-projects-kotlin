package com.ivettevaldez.saturnus.screens.common.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
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

    private val classTag: String = this::class.java.simpleName

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    private lateinit var viewMvc: INavDrawerViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newNavDrawerViewMvc(null)
        viewMvc.setCopyright(getCopyright())

        setContentView(viewMvc.getRootView())

        if (savedInstanceState == null) {
            screensNavigator.toSplash()
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

    override fun onPeopleClicked() {
        screensNavigator.toPeople()
    }

    override fun onInvoicingClicked() {
        screensNavigator.toInvoicing()
    }

    private fun getCopyright(): String {
        try {
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            return String.format(
                getString(R.string.app_copyright_template),
                getString(R.string.app_author_name),
                getString(R.string.app_name),
                versionName
            )
        } catch (ex: PackageManager.NameNotFoundException) {
            Log.e(classTag, "@@@@@ Attempting to get the app version name", ex)
        }
        return ""
    }
}