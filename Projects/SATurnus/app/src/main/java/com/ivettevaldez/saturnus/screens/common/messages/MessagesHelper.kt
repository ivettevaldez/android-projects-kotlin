package com.ivettevaldez.saturnus.screens.common.messages

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MessagesHelper @Inject constructor() {

    fun showShortMessage(rootView: View, @StringRes message: Int) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showShortMessage(rootView: View, message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLongMessage(rootView: View, @StringRes message: Int) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }

    fun showLongMessage(rootView: View, message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
}