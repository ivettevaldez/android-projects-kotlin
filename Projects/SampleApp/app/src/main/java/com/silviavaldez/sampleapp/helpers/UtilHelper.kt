package com.silviavaldez.sampleapp.helpers

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.silviavaldez.sampleapp.R

class UtilHelper {

    fun showView(view: View, show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        view.visibility = visibility
    }

    fun changeStatusBarColor(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor =
                ContextCompat.getColor(activity.applicationContext, R.color.black)
    }
}