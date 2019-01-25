package com.silviavaldez.mlapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.FirebaseApp
import com.silviavaldez.mlapp.R
import kotlinx.android.synthetic.main.activity_main.*

private const val TEXT_RECOGNITION = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        setListenersToViews()
    }

    private fun goToModule(module: Int) {
        when (module) {
            TEXT_RECOGNITION -> {
                val intent = Intent(this, TextRecognitionActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val buttonListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.main_button_recognition -> goToModule(TEXT_RECOGNITION)
        }
    }

    private fun setListenersToViews() {
        main_button_recognition.setOnClickListener(buttonListener)
    }
}
