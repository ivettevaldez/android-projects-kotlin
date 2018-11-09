package com.silviavaldez.sampleapp.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.PreferencesHelper
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import com.silviavaldez.sampleapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_splash.*

private const val DELAY: Long = 2000L

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UtilHelper().changeStatusBarColor(this)
        splash_text_app_name.typeface = TypefaceHelper(this).bold

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
