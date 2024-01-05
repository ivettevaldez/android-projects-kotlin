package com.ivettevaldez.cleanarchitecture.screens.common.dialogs

import com.ivettevaldez.cleanarchitecture.common.BaseObservable

class DialogsEventBus : BaseObservable<DialogsEventBus.Listener>() {

    interface Listener {

        fun onDialogEvent(event: Any)
    }

    fun postEvent(event: Any) {
        for (listener in getListeners()) {
            listener.onDialogEvent(event)
        }
    }
}