package com.ivettevaldez.dependencyinjection.screens.common.viewsmvc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

open class BaseViewMvc<LISTENER_TYPE>(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes layoutId: Int
) {

    val rootView: View = inflater.inflate(layoutId, parent, false)

    protected val context: Context get() = rootView.context
    protected val listeners: MutableSet<LISTENER_TYPE> = HashSet()

    fun registerListener(listener: LISTENER_TYPE) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_TYPE) {
        listeners.remove(listener)
    }

    protected fun <T : View?> findViewById(@IdRes id: Int): T {
        return rootView.findViewById<T>(id)
    }
}