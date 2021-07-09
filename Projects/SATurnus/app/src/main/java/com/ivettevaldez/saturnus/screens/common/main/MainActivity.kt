package com.ivettevaldez.saturnus.screens.common.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val classTag: String = this::class.java.simpleName

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: MainController

    private var consumedOnBackPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        val viewMvc = viewMvcFactory.newNavDrawerViewMvc(null)

        controller = controllerFactory.newMainController()
        controller.bindView(viewMvc)
        controller.bindCopyright(getCopyright())

        setContentView(viewMvc.getRootView())

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
        if (consumedOnBackPress) {
            super.onBackPressed()
        } else {
            consumedOnBackPress = controller.onBackPressed()
        }
    }

    // TODO: Move this method to controller
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