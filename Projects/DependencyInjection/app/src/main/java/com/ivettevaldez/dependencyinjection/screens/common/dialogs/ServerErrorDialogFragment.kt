package com.ivettevaldez.dependencyinjection.screens.common.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.ivettevaldez.dependencyinjection.R

class ServerErrorDialogFragment(private val activity: Activity) : BaseDialog() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).let {
            it.setTitle(R.string.server_error_dialog_title)
            it.setMessage(R.string.server_error_dialog_message)
            it.setPositiveButton(R.string.server_error_dialog_button_caption) { _, _ -> dismiss() }
            it.create()
        }
    }

    companion object {

        fun newInstance(activity: Activity): ServerErrorDialogFragment {
            return ServerErrorDialogFragment(activity)
        }
    }
}