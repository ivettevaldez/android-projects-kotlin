package com.ivettevaldez.saturnus.screens.common.dialogs

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialog
import com.ivettevaldez.saturnus.screens.people.form.PersonFormFragment
import javax.inject.Inject

class DialogsManager @Inject constructor(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    fun showMissingFieldsError(tag: String?) {
        val dialogFragment: DialogFragment = InfoDialog.newInfoDialog(
            getString(R.string.error_saving_title),
            getString(R.string.error_missing_fields),
            getString(R.string.action_ok)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showSavePersonError(tag: String?) {
        val dialogFragment: DialogFragment = InfoDialog.newInfoDialog(
            getString(R.string.error_saving_title),
            getString(R.string.error_saving_message),
            getString(R.string.action_ok)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showInvalidPersonNameError(tag: String?) {
        val dialogFragment: DialogFragment = InfoDialog.newInfoDialog(
            getString(R.string.error_saving_title),
            String.format(
                getString(R.string.error_invalid_person_name_template),
                PersonFormFragment.MIN_NAME_LENGTH
            ),
            getString(R.string.action_ok)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showInvalidRfcError(tag: String?) {
        val dialogFragment: DialogFragment = InfoDialog.newInfoDialog(
            getString(R.string.error_saving_title),
            getString(R.string.error_invalid_rfc),
            getString(R.string.action_ok)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showSelectClientTypeDialog(tag: String?) {
        val bottomSheetDialog: BottomSheetDialogFragment =
            PromptBottomSheetDialog.newPromptBottomSheetDialog(
                getString(R.string.people_select_client_type),
                getString(R.string.people_client_type_issuing),
                getString(R.string.people_client_type_receiver)
            )
        bottomSheetDialog.show(fragmentManager, tag)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments) {
            if (fragment is BaseDialog) {
                return fragment.tag
            }
        }
        return null
    }

    private fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}