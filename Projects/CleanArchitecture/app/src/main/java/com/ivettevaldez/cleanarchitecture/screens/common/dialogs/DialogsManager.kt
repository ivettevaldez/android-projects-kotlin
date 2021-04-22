package com.ivettevaldez.cleanarchitecture.screens.common.dialogs

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptDialog

class DialogsManager(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    fun showUseCaseError(tag: String?) {
        val dialogFragment: DialogFragment = PromptDialog.newPromptDialog(
            getString(R.string.error_network_callback_failed_title),
            getString(R.string.error_network_callback_failed_message),
            getString(R.string.error_network_callback_failed_positive_button_caption),
            getString(R.string.error_network_callback_failed_negative_button_caption)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments) {
            if (fragment is BaseDialog) {
                return fragment.tag
            }
        }
        return null
    }

    private fun getString(stringId: Int): String {
        return context.getString(stringId)
    }
}