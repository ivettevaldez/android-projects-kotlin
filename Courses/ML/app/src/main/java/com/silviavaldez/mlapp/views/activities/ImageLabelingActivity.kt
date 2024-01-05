package com.silviavaldez.mlapp.views.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import com.silviavaldez.mlapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_image_labeling.*
import kotlinx.android.synthetic.main.activity_landmark_recognition.*

class ImageLabelingActivity : AppCompatActivity() {

    private val classTag: String? = ImageLabelingActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    private var utilHelper: UtilHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)

        cameraHelper.setCamera(image_labelling_fab)
        utilHelper = UtilHelper(
            image_labelling_progress,
            image_labelling_fab,
            image_labelling_text_instruction,
            image_labelling_text_title,
            image_labelling_text_content
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionHelper.REQUEST_PERMISSIONS -> {
                val result = cameraHelper.openCamera(grantResults)

                if (result != 0) {
                    Snackbar.make(image_labelling_layout, result, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    utilHelper?.lockViews(true)

                    val photo = cameraHelper.showBitmap(image_labelling_image_photo)
                    if (photo != null) {
                        labelImage(photo)
                    } else {
                        utilHelper?.showProgress(false)
                        Snackbar.make(image_labelling_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getDataFromLabels(labels: List<FirebaseVisionLabel>): String {
        var result = ""

        if (labels.isEmpty()) {
            result += getString(R.string.msg_detection_anything)
        } else {
            for (label in labels) {
                val entity = label.label
                val confidence = label.confidence * 100
//            val entityId = label.entityId

                val info = String.format("- Entity: %s, I'm %.2f%% sure.\n", entity, confidence)
                result += info

                Log.e(classTag, info)
            }
        }

        return result.trim()
    }

    private fun labelImage(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)
        val options = FirebaseVisionLabelDetectorOptions.Builder()
            .setConfidenceThreshold(0.75f)
            .build()

        val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { labels ->
                utilHelper?.lockViews(false)

                val data = getDataFromLabels(labels)
                image_labelling_text_content.text = data
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
                Snackbar.make(landmark_layout, R.string.error_processing_picture, Snackbar.LENGTH_LONG).show()
                utilHelper?.showProgress(false)
            }
    }
}
