package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BackPressDispatcher
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BackPressedListener
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BaseActivity
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.screensnavigator.ScreensNavigator

class MainQuestionsActivity : BaseActivity(),
    NavDrawerViewMvc.Listener,
    NavDrawerHelper,
    BackPressDispatcher,
    FragmentFrameWrapper {

    private lateinit var viewMvc: NavDrawerViewMvc
    private lateinit var screensNavigator: ScreensNavigator

    private val backPressedListeners: MutableSet<BackPressedListener> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screensNavigator = getCompositionRoot().getScreensNavigator()
        viewMvc = getCompositionRoot().getViewMvcFactory().getNavDrawerViewMvc(null)

        setContentView(viewMvc.getRootView())

        if (savedInstanceState == null) {
            screensNavigator.toQuestionsList()
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
        var isBackPressConsumedByAnyListener = false

        for (listener in backPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressConsumedByAnyListener = true
            }
        }

        if (!isBackPressConsumedByAnyListener) {
            super.onBackPressed()
        }
    }

    override fun registerListener(listener: BackPressedListener) {
        backPressedListeners.add(listener)
    }

    override fun unregisterListener(listener: BackPressedListener) {
        backPressedListeners.remove(listener)
    }

    override fun onQuestionsListClicked() {
        screensNavigator.toQuestionsList()
    }

    override fun getFragmentFrame(): FrameLayout = viewMvc.getFragmentFrame()

    override fun isDrawerOpen(): Boolean = viewMvc.isDrawerOpen()

    override fun openDrawer() {
        viewMvc.openDrawer()
    }

    override fun closeDrawer() {
        viewMvc.closeDrawer()
    }
}