package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.SignOutHelper
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

private const val LIST_AND_FORM = 1
private const val CAMERA = 2
private const val ACCELEROMETER = 3

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        TypefaceHelper(this).overrideAllTypefaces()

        setUpActionBar()
        setListenersToViews()
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

    private fun setUpActionBar() {
        val layoutParams = RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT)

        val textView = TextView(applicationContext)
        textView.layoutParams = layoutParams
        textView.text = title
        textView.textSize = 20f
        textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))

        textView.typeface = TypefaceHelper(this).bold

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.customView = textView
    }

    private fun goToModule(module: Int) {
        val intent: Intent = when (module) {
            LIST_AND_FORM -> Intent(this, ListActivity::class.java)
            CAMERA -> Intent(this, CameraActivity::class.java)
            ACCELEROMETER -> Intent(this, AccelerometerActivity::class.java)
            else -> Intent(this, ListActivity::class.java)
        }

        startActivity(intent)
        AnimationHelper().enterTransition(this)
    }

    private val buttonListener = View.OnClickListener { view ->
        val module: Int = when (view.id) {
            R.id.main_button_list -> LIST_AND_FORM
            R.id.main_button_camera -> CAMERA
            R.id.main_button_accelerometer -> ACCELEROMETER
            else -> LIST_AND_FORM
        }

        goToModule(module)
    }

    private fun setListenersToViews() {
        main_button_list.setOnClickListener(buttonListener)
        main_button_camera.setOnClickListener(buttonListener)
        main_button_accelerometer.setOnClickListener(buttonListener)
    }
}
