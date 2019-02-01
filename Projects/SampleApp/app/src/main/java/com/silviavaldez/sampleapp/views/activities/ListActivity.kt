package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import com.silviavaldez.sampleapp.models.daos.PersonDao
import com.silviavaldez.sampleapp.views.adapters.ListAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpAddButton()
        setUpTypefaces()
    }

    override fun onResume() {
        super.onResume()
        setUpList()
    }

    private fun setUpList() {
        val patients = PersonDao().findAllPeople()

        if (patients.size != 0) {
            list_text_instruction.visibility = View.GONE
            list_items.adapter = ListAdapter(this, patients)
        } else {
            list_text_instruction.visibility = View.VISIBLE
        }
    }

    private fun setUpAddButton() {
        list_fab_add.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }
    }

    private fun setUpTypefaces() {
        val typefaceHelper = TypefaceHelper(this)
        typefaceHelper.setUpActionBar(title.toString(), true)

        list_text_instruction.typeface = typefaceHelper.bold
    }
}
