package com.silviavaldez.ressiapp.views.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.silviavaldez.ressiapp.R

const val DELAY_TIME = 2000L

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        waitAMoment()
    }

    private fun waitAMoment() {
        Handler().postDelayed({
            finish()
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, DELAY_TIME)
    }

}
