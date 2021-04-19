package com.ivettevaldez.cleanarchitecture.screens.common.views

import android.content.Context
import android.view.View

abstract class BaseViewMvc : IViewMvc {

    private lateinit var rootView: View

    final override fun getRootView(): View = rootView

    open fun setRootView(rootView: View) {
        this.rootView = rootView
    }

    protected fun getContext(): Context {
        return getRootView().context
    }
}