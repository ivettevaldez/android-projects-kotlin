package com.ivettevaldez.saturnus.screens.common.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class MainActivity : BaseActivity(),
    INavDrawerHelper,
    IFragmentFrameWrapper {

    private val classTag: String = this::class.java.simpleName

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        val viewMvc = viewMvcFactory.newNavDrawerViewMvc(null)

        controller = controllerFactory.newMainController()
        controller.bindView(viewMvc)
        controller.bindCopyright(getCopyright())

        setContentView(viewMvc.getRootView())

        // TODO: Is it possible to mock savedInstanceState?
        if (savedInstanceState == null) {
            controller.toSplash()
        }
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }

    override fun onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun isDrawerOpen(): Boolean = controller.isDrawerOpen()

    override fun openDrawer() {
        controller.openDrawer()
    }

    override fun closeDrawer() {
        controller.closeDrawer()
    }

    override fun getFragmentFrame(): FrameLayout = controller.getFragmentFrame()

    private fun getCopyright(): String = String.format(
        getString(R.string.app_copyright_template),
        getString(R.string.app_author_name),
        getString(R.string.app_name),
        getVersionName()
    )

    // TODO: Is it possible to mock this method?
    private fun getVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (ex: PackageManager.NameNotFoundException) {
            Log.e(classTag, "@@@@@ Attempting to get the app version name", ex)
            ""
        }
    }
}