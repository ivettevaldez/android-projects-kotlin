package com.ivettevaldez.saturnus.screens.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class SplashFragment : BaseFragment() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var uiHandler: Handler

    companion object {

        @JvmStatic
        fun newInstance() = SplashFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newSplashViewMvc(parent)

        uiHandler.postDelayed({
            screensNavigator.toInvoiceIssuingPeople()
        }, Constants.SPLASH_NAVIGATION_DELAY)

        return viewMvc.getRootView()
    }
}