package com.silviavaldez.sampleapp.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.provider.Settings
import android.text.InputType
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.delegates.IInputAlertDelegate
import com.silviavaldez.sampleapp.helpers.delegates.ISimpleAlertDelegate

private const val LOCATION_SETTINGS_ID = 666
private const val EDIT_TEXT_MARGIN = 20

class AlertDialogHelper(private val activity: Activity) {

    private var alertDialog: AlertDialog? = null
    private var alertWithInputField: AlertDialog? = null

    fun showNetworkProblemDialog() {
        val title = activity.getString(R.string.error_title_no_internet_connection)
        val message = activity.getString(R.string.error_msg_no_internet_connection)
        val positiveText = activity.getString(R.string.action_retry)
        val negativeText = activity.getString(R.string.action_cancel)

        // TODO: Must retry the request
        showSimpleAlert(title, message, positiveText, negativeText)
    }

    fun showLocationSettingsDialog() {
        val title = activity.getString(R.string.error_title_disabled_gps)
        val message = activity.getString(R.string.error_msg_disabled_gps)
        val positiveText = activity.getString(android.R.string.yes)
        val negativeText = activity.getString(android.R.string.no)

        // TODO: Must call openLocationSettings
        showSimpleAlert(title, message, positiveText, negativeText)
    }

    fun showDefaultInputDialog() {
        val title = activity.resources.getString(R.string.error_title_empty_field)
        val message = activity.resources.getString(R.string.error_msg_empty_field)
        val positiveText = activity.getString(android.R.string.yes)
        val negativeText = activity.getString(android.R.string.no)

        // Activity must implement IInputAlertDelegate
        showInputAlert(InputType.TYPE_CLASS_TEXT, title, message, positiveText, negativeText)
    }

    private fun dpToPx(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun addEditText(activity: Activity, type: Int): EditText {
        val margin = dpToPx(EDIT_TEXT_MARGIN)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(margin, 0, margin, 0)

        val input = EditText(activity)
        input.inputType = type
        input.layoutParams = params
//        val container = FrameLayout(activity)
//        container.addView(input)

        return input
    }

    private fun validateInput(strInput: String) {
        if (strInput.isBlank()) {
            // Prevent an empty field
            UtilHelper().showShortToast(activity,
                    activity.getString(R.string.error_empty_field))
        } else {
            val delegate = activity as IInputAlertDelegate
            delegate.onInputText(strInput)
            alertWithInputField?.dismiss()
        }
    }

    private fun showInputAlert(inputType: Int, title: String, message: String,
                               positiveText: String, negativeText: String) {
        val input = addEditText(activity, inputType)

        alertWithInputField?.cancel()
        alertWithInputField = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setView(input)
                .setNegativeButton(negativeText) { dialog, which ->
                    alertWithInputField?.dismiss()
                }
                .setPositiveButton(positiveText) { dialog, which ->
                    val strInput = input.text.toString()
                    validateInput(strInput)
                }.show()
    }

    private fun showSimpleAlert(title: String, message: String,
                                positiveText: String, negativeText: String) {
        val delegate = activity as ISimpleAlertDelegate

        alertDialog?.cancel()
        alertDialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeText) { dialog, which ->
                    delegate.onNegativeAction()
                    alertDialog?.dismiss()
                }
                .setPositiveButton(positiveText) { dialog, which ->
                    delegate.onPositiveAction()
                    alertDialog?.dismiss()
                }.show()
    }

    private fun openLocationSettings(activity: Activity): Runnable {
        return Runnable {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivityForResult(intent, LOCATION_SETTINGS_ID)
        }
    }
}