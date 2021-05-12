package com.ivettevaldez.saturnus.screens.common.dialogs

import android.content.Context
import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class DialogsManager @Inject constructor(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

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