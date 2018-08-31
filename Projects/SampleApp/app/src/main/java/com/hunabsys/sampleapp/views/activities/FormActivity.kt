package com.hunabsys.sampleapp.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.AnimationHelper
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpGenders()
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

    private fun setUpGenders() {
        val genders = resources.getStringArray(R.array.form_genders)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genders)
        form_spinner_gender.adapter = adapter
    }
}
