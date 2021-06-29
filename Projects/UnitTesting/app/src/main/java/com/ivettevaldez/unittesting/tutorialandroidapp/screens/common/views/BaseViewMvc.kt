package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views

import android.content.Context
import android.view.View
import androidx.annotation.StringRes

open class BaseViewMvc : ViewMvc {

    private var rootView: View? = null

    override fun getRootView(): View? {
        return rootView
    }

    protected fun setRootView(rootView: View?) {
        this.rootView = rootView
    }

    protected fun <T : View?> findViewById(id: Int): T {
        return getRootView()!!.findViewById(id)
    }

    protected fun getContext(): Context {
        return getRootView()!!.context
    }

    protected fun getString(@StringRes id: Int): String {
        return getContext().getString(id)
    }
}