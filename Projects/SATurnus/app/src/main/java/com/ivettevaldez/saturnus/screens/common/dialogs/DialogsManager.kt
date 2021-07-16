package com.ivettevaldez.saturnus.screens.common.dialogs

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.dialogs.info.InfoDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.IPersonSelectorBottomSheetViewMvc
import com.ivettevaldez.saturnus.screens.common.dialogs.personselector.PersonSelectorBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialog
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialog
import com.ivettevaldez.saturnus.screens.people.form.PersonFormFragment
import javax.inject.Inject

open class DialogsManager @Inject constructor(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    open fun showGenericSavingError(tag: String?, error: String) {
        val dialogFragment: DialogFragment = InfoDialog.newInfoDialog(
            getString(R.string.error_saving_title),
            error,
            getString(R.string.action_ok)
        )
        dialogFragment.show(fragmentManager, tag)
    }

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

    open fun showExitWithoutSavingChangesConfirmation(tag: String?) {
        val dialogFragment: PromptDialog =
            PromptDialog.newPromptDialog(
                getString(R.string.action_exit),
                getString(R.string.message_exit_without_saving_confirmation),
                getString(R.string.action_exit),
                getString(R.string.action_cancel)
            )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showDeletePersonConfirmation(tag: String?) {
        val dialogFragment: DialogFragment = PromptDialog.newPromptDialog(
            getString(R.string.action_delete_person),
            getString(R.string.message_delete_person_confirmation),
            getString(R.string.action_delete),
            getString(R.string.action_cancel)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun showPersonOptionsDialog(tag: String?) {
        val bottomSheetDialog: BottomSheetDialogFragment =
            PromptBottomSheetDialog.newPromptBottomSheetDialog(
                getString(R.string.message_what_action_do_you_want),
                getString(R.string.action_edit_person),
                getString(R.string.action_delete_person)
            )
        bottomSheetDialog.show(fragmentManager, tag)
    }

    open fun showPersonSelectorDialog(
        tag: String?,
        listener: IPersonSelectorBottomSheetViewMvc.Listener
    ) {
        val bottomSheetDialog: BottomSheetDialogFragment =
            PersonSelectorBottomSheetDialog.newPersonSelectorBottomSheetDialog(listener)
        bottomSheetDialog.show(fragmentManager, tag)
    }

    fun showDeleteInvoiceConfirmation(tag: String?) {
        val dialogFragment: DialogFragment = PromptDialog.newPromptDialog(
            getString(R.string.action_delete_invoice),
            getString(R.string.message_delete_invoice_confirmation),
            getString(R.string.action_delete),
            getString(R.string.action_cancel)
        )
        dialogFragment.show(fragmentManager, tag)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments) {
            if (fragment is BaseDialog || fragment is BaseBottomSheetDialog) {
                return fragment.tag
            }
        }
        return null
    }

    private fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}