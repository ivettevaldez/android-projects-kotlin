package com.ivettevaldez.saturnus.screens.common.dialogs

import com.ivettevaldez.saturnus.common.observable.BaseObservable
import javax.inject.Inject

class DialogsEventBus @Inject constructor() : BaseObservable<DialogsEventBus.Listener>() {

    interface Listener {

        fun onDialogEvent(event: Any)
    }

    fun postEvent(event: Any) {
        for (listener in listeners) {
            listener.onDialogEvent(event)
        }
    }
}