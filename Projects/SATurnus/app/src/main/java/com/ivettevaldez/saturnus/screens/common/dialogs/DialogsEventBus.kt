package com.ivettevaldez.saturnus.screens.common.dialogs

import com.ivettevaldez.saturnus.common.dependencyinjection.activity.ActivityScope
import com.ivettevaldez.saturnus.common.observable.BaseObservable
import javax.inject.Inject

@ActivityScope
open class DialogsEventBus @Inject constructor() : BaseObservable<DialogsEventBus.Listener>() {

    interface Listener {

        fun onDialogEvent(event: Any)
    }

    open fun postEvent(event: Any) {
        for (listener in listeners) {
            listener.onDialogEvent(event)
        }
    }
}