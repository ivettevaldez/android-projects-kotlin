package com.hunabsys.sampleapp.helpers

import android.app.Activity

/**
 * Helper class for activities animations and transitions.
 * Created by Silvia Valdez on 1/18/18.
 */
class AnimationHelper {

    fun enterTransition(activity: Activity) {
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
    }

    fun exitTransition(activity: Activity) {
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}