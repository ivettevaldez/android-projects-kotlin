package com.ivettevaldez.cleanarchitecture.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.INavDrawerViewMvc
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

class MainActivity : BaseActivity(),
    IFragmentFrameWrapper,
    INavDrawerHelper,
    INavDrawerViewMvc.Listener {

    private lateinit var viewMvc: INavDrawerViewMvc
    private lateinit var screenNavigator: ScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenNavigator = getCompositionRoot().getScreenNavigator()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getNavDrawerViewMvc(null)

        setContentView(viewMvc.getRootView())

        if (savedInstanceState == null) {
            screenNavigator.toQuestionsList()
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

    override fun onQuestionsListClicked() {
        screenNavigator.toQuestionsList()
    }
}