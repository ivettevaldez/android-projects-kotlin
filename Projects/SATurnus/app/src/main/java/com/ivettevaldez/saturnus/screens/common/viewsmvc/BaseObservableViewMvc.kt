package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

open class BaseObservableViewMvc<LISTENER_TYPE>(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes layoutId: Int
) : BaseViewMvc(inflater, parent, layoutId),
    IObservableViewMvc<LISTENER_TYPE> {

    protected val listeners: MutableSet<LISTENER_TYPE> = HashSet()

    override fun registerListener(listener: LISTENER_TYPE) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: LISTENER_TYPE) {
        listeners.remove(listener)
    }
}