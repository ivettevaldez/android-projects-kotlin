package com.silviavaldez.sampleapp.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.TouchDelegate
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import com.silviavaldez.sampleapp.R

const val SNACK_COLOR_SUCCESS = R.color.spring_green
const val SNACK_COLOR_WARNING = R.color.supernova
const val SNACK_COLOR_ERROR = R.color.bittersweet
const val SNACK_COLOR_DEFAULT = R.color.mulled_wine

class UtilHelper {

    private var toast: Toast? = null
    private var alertDialog: AlertDialog? = null

    companion object {
        const val SNACK_TYPE_SUCCESS = 1
        const val SNACK_TYPE_WARNING = 2
        const val SNACK_TYPE_ERROR = 3
        const val SNACK_TYPE_DEFAULT = 4
    }

    fun showView(view: View, show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        view.visibility = visibility
    }

    fun showShortToast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun showLongToast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    fun showCustomSnackBar(context: Context, snack: Snackbar, type: Int) {
        val color = when (type) {
            SNACK_TYPE_SUCCESS -> SNACK_COLOR_SUCCESS
            SNACK_TYPE_WARNING -> SNACK_COLOR_WARNING
            SNACK_TYPE_ERROR -> SNACK_COLOR_ERROR

            else -> SNACK_COLOR_DEFAULT
        }
        snack.view.setBackgroundColor(context.getColor(color))
        snack.show()
    }

    private fun showExitAlert(activity: Activity, title: String, message: String) {
        if (alertDialog != null) {
            alertDialog?.cancel()
        }

        alertDialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.action_exit) { dialog, which ->
                    activity.finish()
                    AnimationHelper().exitTransition(activity)
                }
                .setNegativeButton(R.string.action_cancel) { dialog, which ->
                    dialog.dismiss()
                }.show()
    }

    fun showExitDialog(activity: Activity) {
        val title = activity.getString(R.string.action_exit)
        val message = activity.getString(R.string.message_unsaved_changes)
        UtilHelper().showExitAlert(activity, title, message)
    }

    fun enlargeHitArea(button: ImageButton) {
        // Enlarge hit area for the button
        val buttonParent = button.parent as View  // The view to enlarge hit area
        buttonParent.post {
            val rect = Rect()
            button.getHitRect(rect)
            rect.top -= 100    // increase top hit area
            rect.left -= 100   // increase left hit area
            rect.bottom += 100 // increase bottom hit area
            rect.right += 100  // increase right hit area
            buttonParent.touchDelegate = TouchDelegate(rect, button)
        }
    }

    fun changeStatusBarColor(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor =
                ContextCompat.getColor(activity.applicationContext, R.color.black)
    }
}