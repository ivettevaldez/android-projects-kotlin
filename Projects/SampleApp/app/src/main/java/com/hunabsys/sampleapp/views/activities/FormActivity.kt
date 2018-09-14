package com.hunabsys.sampleapp.views.activities

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.AnimationHelper
import kotlinx.android.synthetic.main.activity_form.*

private const val DELAY = 1000L

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpGenders()
        setUpSaveButton()
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

    private fun hasEmptyFields(): Boolean {
        return form_edit_name.text.isBlank() ||
                form_edit_last_name.text.isBlank() ||
                form_edit_email.text.isBlank() ||
                form_edit_age.text.isBlank() ||
                form_edit_address.text.isBlank() ||
                form_edit_city.text.isBlank() ||
                form_edit_country.text.isBlank() ||
                form_edit_job.text.isBlank()
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            form_progress.visibility = View.VISIBLE
            form_button_save.isEnabled = false
        } else {
            form_progress.visibility = View.GONE
            form_button_save.isEnabled = true
        }
    }

    private fun saveData() {
        // TODO: Missing implementation of a real database
        showProgress(true)

        // Dummy functionality
        Handler().postDelayed({
            showProgress(false)

            Snackbar.make(form_layout,
                    getString(R.string.message_all_saved),
                    Snackbar.LENGTH_SHORT).show()

            Handler().postDelayed({
                finish()
            }, DELAY)
        }, DELAY)
    }

    private fun setUpSaveButton() {
        form_button_save.setOnClickListener {
            if (hasEmptyFields()) {
                Snackbar.make(form_layout,
                        getString(R.string.error_all_fields_required),
                        Snackbar.LENGTH_SHORT).show()
            } else {
                saveData()
            }
        }
    }
}
