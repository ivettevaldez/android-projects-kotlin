package com.ivettevaldez.saturnus.screens.common.main

import android.os.Bundle
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: MainViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newMainViewMvc(null)
        setContentView(viewMvc.rootView)
    }
}