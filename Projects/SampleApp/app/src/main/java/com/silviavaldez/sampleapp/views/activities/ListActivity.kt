package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.models.daos.PersonDao
import com.silviavaldez.sampleapp.views.adapters.ListAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpAddButton()
    }

    override fun onResume() {
        super.onResume()
        setUpList()
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

    private fun setUpList() {
        val patients = PersonDao().findAllPeople()

        if (patients.size != 0) {
            list_text_nothing_to_show.visibility = View.GONE
            list_items.adapter = ListAdapter(this, patients)
        } else {
            list_text_nothing_to_show.visibility = View.VISIBLE
        }
    }

    private fun setUpAddButton() {
        list_fab_add.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }
    }
}
