package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.toasts

import android.content.Context
import android.widget.Toast
import com.ivettevaldez.unittesting.R

class ToastsHelper(private val context: Context) {

    fun showUseCaseError() {
        Toast.makeText(context, R.string.error_network_call_failed, Toast.LENGTH_SHORT).show()
    }
}