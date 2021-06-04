package com.ivettevaldez.saturnus.screens.common.controllers

import com.ivettevaldez.saturnus.common.dependencyinjection.activity.ActivityScope
import com.ivettevaldez.saturnus.common.observable.BaseObservable
import javax.inject.Inject

@ActivityScope
class FragmentsEventBus @Inject constructor() : BaseObservable<FragmentsEventBus.Listener>() {

    interface Listener {

        fun onFragmentEvent(event: Any)
    }

    fun postEvent(event: Any) {
        for (listener in listeners) {
            listener.onFragmentEvent(event)
        }
    }
}