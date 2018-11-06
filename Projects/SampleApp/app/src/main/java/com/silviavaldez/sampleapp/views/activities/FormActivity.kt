package com.silviavaldez.sampleapp.views.activities

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.models.daos.PersonDao
import com.silviavaldez.sampleapp.models.datamodels.Person
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
        return form_edit_name.text!!.isBlank() ||
                form_edit_last_name.text!!.isBlank() ||
                form_edit_email.text!!.isBlank() ||
                form_edit_age.text!!.isBlank() ||
                form_edit_address.text!!.isBlank() ||
                form_edit_city.text!!.isBlank() ||
                form_edit_country.text!!.isBlank() ||
                form_edit_job.text!!.isBlank() ||
                form_spinner_gender.selectedItemPosition == 0
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

    private fun createPerson() {
        val person = Person()
        val personDao = PersonDao()

        person.id = personDao.getNextId()
        person.name = form_edit_name.text.toString()
        person.lastName = form_edit_last_name.text.toString()
        person.email = form_edit_email.text.toString()
        person.age = form_edit_age.text.toString().toInt()
        person.address = form_edit_address.text.toString()
        person.city = form_edit_city.text.toString()
        person.country = form_edit_country.text.toString()
        person.profession = form_edit_job.text.toString()
        person.gender = form_spinner_gender.selectedItemPosition

        personDao.create(person)
    }

    private fun savePerson() {
        showProgress(true)
        createPerson()

        // A bit of lag to show progress
        Handler().postDelayed({
            showProgress(false)

            Snackbar.make(form_layout,
                    getString(R.string.message_all_saved),
                    Snackbar.LENGTH_SHORT).show()

            // A bit of lag to show the success message
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
                savePerson()
            }
        }
    }
}
