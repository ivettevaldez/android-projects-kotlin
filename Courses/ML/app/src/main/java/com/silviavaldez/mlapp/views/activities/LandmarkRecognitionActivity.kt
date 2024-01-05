package com.silviavaldez.mlapp.views.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import com.silviavaldez.mlapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_face_detection.*
import kotlinx.android.synthetic.main.activity_landmark_recognition.*

class LandmarkRecognitionActivity : AppCompatActivity() {

    private val classTag: String? = LandmarkRecognitionActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    private var utilHelper: UtilHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_recognition)

        cameraHelper.setCamera(landmark_fab)
        utilHelper = UtilHelper(
            landmark_progress,
            landmark_fab,
            landmark_text_instruction,
            null,
            landmark_text_content
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
                    Snackbar.make(landmark_layout, result, Snackbar.LENGTH_SHORT).show()
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

                    val photo = cameraHelper.showBitmap(landmark_image_photo)
                    if (photo != null) {
                        Snackbar.make(landmark_layout, R.string.msg_please_wait, Snackbar.LENGTH_LONG)
                            .show()

                        findLandmark(photo)
                    } else {
                        utilHelper?.showProgress(false)
                        Snackbar.make(landmark_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getDataFromLandmarks(landmarks: List<FirebaseVisionCloudLandmark>): String {
        var result = ""

        if (landmarks.isEmpty()) {
            result += getString(R.string.msg_detection_anything)
        } else {
            for (landmark in landmarks) {
                val entityId = landmark.entityId
                val bounds = landmark.boundingBox
                val name = landmark.landmark
                val confidence = landmark.confidence

                // Multiple locations are possible, e.g., the location of the depicted
                // landmark and the location the picture was taken.
                for (loc in landmark.locations) {
                    val latitude = loc.latitude
                    val longitude = loc.longitude
                }

                val info = String.format("- Name: %s, I'm %.2f%% sure.\n", name, confidence)
                result += info

                Log.e(classTag, "Name: $name, ID: $entityId, ")
            }
        }

        return result.trim()
    }

    private fun findLandmark(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)
        val options = FirebaseVisionCloudDetectorOptions.Builder()
            .setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL)
            .setMaxResults(5)
            .build()

        val detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { landmarks ->
                utilHelper?.lockViews(false)

                val data = getDataFromLandmarks(landmarks)
                landmark_text_content.text = data
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
                Snackbar.make(landmark_layout, R.string.error_processing_picture, Snackbar.LENGTH_LONG).show()
                utilHelper?.showProgress(false)
            }
    }
}
