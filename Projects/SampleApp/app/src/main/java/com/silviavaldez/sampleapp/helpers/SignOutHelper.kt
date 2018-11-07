package com.silviavaldez.sampleapp.helpers

import android.app.Activity
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.services.delegates.ISignOutDelegate
import com.silviavaldez.sampleapp.services.rest.SignOutService
import com.silviavaldez.sampleapp.views.activities.LoginActivity

class SignOutHelper(private val activity: Activity) : ISignOutDelegate {

    private val tag: String = SignOutHelper::class.java.simpleName

    private var alertDialog: AlertDialog? = null

    override fun onSignOutSuccess() {
        closeSession()
    }

    override fun onSignOutFailure(error: String) {
        Log.e(tag, error)
        closeSession()
    }

    private fun closeSession() {
        PreferencesHelper(activity).logout()

        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()

        AnimationHelper().exitTransition(activity)
    }

    private fun signOut(rootView: View, progressView: View) {
        // Use background threads to lighten the job to the UI thread.
        Thread {
            val isSigningOut = SignOutService(activity, this).signOut()

            activity.runOnUiThread {
                if (!isSigningOut) {
                    Snackbar.make(rootView,
                            R.string.error_msg_no_internet_connection,
                            Snackbar.LENGTH_LONG).show()
                } else {
                    UtilHelper().showView(progressView, true)
                }
            }
        }.start()
    }

    fun showLogoutAlert(rootView: View, progressView: View) {
        if (alertDialog != null) {
            alertDialog?.cancel()
        }

        val message = activity.getString(R.string.message_logout_confirmation)

        alertDialog = AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.action_logout) { dialog, which ->
                    signOut(rootView, progressView)
                }
                .setNegativeButton(R.string.action_cancel) { dialog, which ->
                    dialog.dismiss()
                }.show()
    }
}