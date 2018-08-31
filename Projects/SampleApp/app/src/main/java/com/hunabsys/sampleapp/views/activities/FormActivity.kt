package com.hunabsys.sampleapp.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.AnimationHelper

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
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
