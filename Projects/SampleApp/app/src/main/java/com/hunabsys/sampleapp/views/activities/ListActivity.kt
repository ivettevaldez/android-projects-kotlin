package com.hunabsys.sampleapp.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.AnimationHelper
import com.hunabsys.sampleapp.pojos.ItemList
import com.hunabsys.sampleapp.views.adapters.ListAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        val items = ArrayList<ItemList>()
        var item: ItemList

        for (i in 1..10) {
            item = ItemList("Dummy title $i", "Also, a dummy subtitle $i")
            items.add(item)
        }

        list_items.adapter = ListAdapter(this, items)
    }
}
