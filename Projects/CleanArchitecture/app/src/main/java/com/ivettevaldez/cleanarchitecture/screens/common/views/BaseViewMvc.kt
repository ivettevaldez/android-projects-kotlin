package com.ivettevaldez.cleanarchitecture.screens.common.views

import android.content.Context
import android.view.View

abstract class BaseViewMvc : IViewMvc {

    private lateinit var rootView: View

    override fun getRootView(): View = rootView

    protected fun setRootView(rootView: View) {
        this.rootView = rootView
    }

    protected fun getContext(): Context {
        return getRootView().context
    }
}