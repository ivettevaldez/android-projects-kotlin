package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

open class BaseViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes layoutId: Int
) : IViewMvc {

    val rootView = inflater.inflate(layoutId, parent, false)

    protected val context get(): Context = rootView.context

    override fun <T : View?> findViewById(@IdRes id: Int): T {
        return rootView.findViewById<T>(id)
    }
}