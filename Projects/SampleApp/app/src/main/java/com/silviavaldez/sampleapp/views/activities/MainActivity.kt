package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.SignOutHelper
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)

        setListenersToMenuButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_logout) {
            SignOutHelper(this).showLogoutAlert(main_layout, main_progress)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setListenersToMenuButtons() {
        // List & form
        main_button_list.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }

        // Accelerometer
        main_button_accelerometer.setOnClickListener {
            val intent = Intent(this, AccelerometerActivity::class.java)
            startActivity(intent)
            AnimationHelper().enterTransition(this)
        }
    }
}
