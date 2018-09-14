package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.pojos.ItemList
import com.silviavaldez.sampleapp.views.adapters.ListAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpList()
        setUpAddButton()
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
        val items = ArrayList<ItemList>()
        var item: ItemList

        for (i in 1..10) {
            item = ItemList("Dummy title $i", "Also, a dummy subtitle $i")
            items.add(item)
        }

        list_items.adapter = ListAdapter(this, items)
    }

    private fun setUpAddButton() {
        list_fab_add.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }
    }
}
