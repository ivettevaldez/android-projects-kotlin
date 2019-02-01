package com.silviavaldez.sampleapp.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        AnimationHelper().exitTransition(this)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AnimationHelper().exitTransition(this)
    }
}