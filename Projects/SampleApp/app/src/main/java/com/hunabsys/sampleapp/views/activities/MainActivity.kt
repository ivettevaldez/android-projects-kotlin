package com.hunabsys.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.helpers.AnimationHelper
import com.hunabsys.sampleapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListenersToMenuButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_logout) {
            UtilHelper().showLogoutAlert(this, main_progress)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListenersToMenuButtons() {
        // Form
        main_button_form.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }

        // List
        main_button_list.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }
    }
}
