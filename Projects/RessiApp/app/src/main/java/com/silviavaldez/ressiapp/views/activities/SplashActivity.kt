package com.silviavaldez.ressiapp.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.silviavaldez.ressiapp.R
import com.silviavaldez.ressiapp.helpers.AnimationHelper
import com.silviavaldez.ressiapp.helpers.PreferencesHelper
import com.silviavaldez.ressiapp.helpers.TypefaceHelper
import kotlinx.android.synthetic.main.activity_splash.*

const val NAVIGATION_DELAY = 2000L

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setUpTypefaces()

        Handler().postDelayed({
            validateSession()
        }, NAVIGATION_DELAY)
    }

    private fun setUpTypefaces() {
        val light = TypefaceHelper(this).robotoLight
        splash_text_copyright.typeface = light
    }

    private fun goToNextScreen(intent: Intent) {
        startActivity(intent)
        finish()
        AnimationHelper().exitTransition(this)
    }

    private fun validateSession() {
        val activeSession = PreferencesHelper(this).activeSession

        val intent = if (activeSession) {
            // ToDo: Redirect to DashboardActivity
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        goToNextScreen(intent)
    }
}
