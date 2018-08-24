package com.hunabsys.sampleapp.helpers

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.views.activities.LoginActivity

class UtilHelper {

    private var alertDialog: AlertDialog? = null

    fun showView(view: View, show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        view.visibility = visibility
    }

    fun changeStatusBarColor(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor =
                ContextCompat.getColor(activity.applicationContext, R.color.black)
    }

    fun showLogoutAlert(activity: Activity, progressView: View) {
        if (alertDialog != null) {
            alertDialog?.cancel()
        }

        val message = activity.getString(R.string.message_logout_confirmation)

        alertDialog = AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.action_logout) { dialog, which ->
                    showView(progressView, true)

                    Handler().postDelayed({
                        PreferencesHelper(activity.applicationContext).logout()
                        showView(progressView, false)

                        val intent = Intent(activity.applicationContext, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.finish()

                        AnimationHelper().exitTransition(activity)
                    }, 1000)
                }
                .setNegativeButton(R.string.action_cancel) { dialog, which ->
                    dialog.dismiss()
                }.show()
    }
}