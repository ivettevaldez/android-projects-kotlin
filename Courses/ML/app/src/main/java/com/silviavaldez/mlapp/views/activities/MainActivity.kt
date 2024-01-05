package com.silviavaldez.mlapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.FirebaseApp
import com.silviavaldez.mlapp.R
import kotlinx.android.synthetic.main.activity_main.*

private const val TEXT_RECOGNITION = 1
private const val FACE_DETECTION = 2
private const val IMAGE_LABELLING = 3
private const val LANDMARK_RECOGNITION = 4

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        setListenersToViews()
    }

    private fun goToModule(module: Int) {
        val intent: Intent = when (module) {
            TEXT_RECOGNITION -> {
                Intent(this, TextRecognitionActivity::class.java)
            }
            FACE_DETECTION -> {
                Intent(this, FaceDetectionActivity::class.java)
            }
            IMAGE_LABELLING -> {
                Intent(this, ImageLabelingActivity::class.java)
            }
            LANDMARK_RECOGNITION -> {
                Intent(this, LandmarkRecognitionActivity::class.java)
            }
            else -> {
                Intent(this, TextRecognitionActivity::class.java)
            }
        }
        startActivity(intent)
    }

    private val buttonListener = View.OnClickListener { view ->
        val module: Int = when (view.id) {
            R.id.main_button_recognition -> {
                TEXT_RECOGNITION
            }
            R.id.main_button_face_detection -> {
                FACE_DETECTION
            }
            R.id.main_button_image_labelling -> {
                IMAGE_LABELLING
            }
            R.id.main_button_landmark -> {
                LANDMARK_RECOGNITION
            }
            else -> {
                TEXT_RECOGNITION
            }
        }
        goToModule(module)
    }

    private fun setListenersToViews() {
        main_button_recognition.setOnClickListener(buttonListener)
        main_button_face_detection.setOnClickListener(buttonListener)
        main_button_image_labelling.setOnClickListener(buttonListener)
        main_button_landmark.setOnClickListener(buttonListener)
    }
}
