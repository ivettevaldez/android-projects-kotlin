package com.ivettevaldez.saturnus.screens.common.main

import android.os.Bundle
import android.widget.Toast
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class MainActivity : BaseActivity(),
    INavDrawerViewMvc.Listener,
    INavDrawerHelper {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: INavDrawerViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newNavDrawerViewMvc(null)
        setContentView(viewMvc.getRootView())
    }

    override fun onOptionClicked() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
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

    override fun isDrawerOpen(): Boolean = viewMvc.isDrawerOpen()

    override fun openDrawer() {
        viewMvc.openDrawer()
    }

    override fun closeDrawer() {
        viewMvc.closeDrawer()
    }
}