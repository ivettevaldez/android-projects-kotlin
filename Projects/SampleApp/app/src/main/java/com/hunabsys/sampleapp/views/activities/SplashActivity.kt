package com.hunabsys.sampleapp.views.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hunabsys.sampleapp.helpers.AnimationHelper
import com.hunabsys.sampleapp.helpers.PreferencesHelper
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.UtilHelper

private const val DELAY: Long = 2000L

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UtilHelper().changeStatusBarColor(this)

        Handler().postDelayed({
            validateSession()
        }, DELAY)
    }

    private fun validateSession() {
        val intent: Intent = if (PreferencesHelper(this).activeSession) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        goToNextScreen(intent)
    }

    private fun goToNextScreen(intent: Intent) {
        startActivity(intent)
        finish()
        AnimationHelper().exitTransition(this)
    }
}
