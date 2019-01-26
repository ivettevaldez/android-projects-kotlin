package com.silviavaldez.mlapp.views.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.FileHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import kotlinx.android.synthetic.main.activity_face_detection.*

class FaceDetectionActivity : AppCompatActivity() {

    private val classTag: String? = FaceDetectionActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)

        cameraHelper.setCamera(face_detection_fab)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionHelper.REQUEST_PERMISSIONS ->
                // If request is cancelled, the result arrays are empty.
                if (PermissionHelper(this).validatePermissionResult(grantResults)) {
                    Log.e(classTag, "Permission granted! :D")

                    val startedIntent = cameraHelper.startCameraIntent()
                    if (!startedIntent) {
                        Snackbar.make(face_detection_layout, R.string.error_opening_camera, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e(classTag, "Permission denied :(")
                    Snackbar.make(
                        face_detection_layout,
                        R.string.error_missing_permissions, Snackbar.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Hide instruction
                    face_detection_text_instruction.visibility = View.GONE

                    val photo = showBitmap()
                    if (photo != null) {
                        detectFaces(photo)
                    }
                }
            }
        }
    }

    private fun showBitmap(): Bitmap? {
        var photo: Bitmap? = null

        try {
            photo = BitmapFactory.decodeFile(FileHelper.imageFilePath)
            face_detection_image_photo.setImageBitmap(photo)

            // Show text
            face_detection_text_title.visibility = View.VISIBLE
            face_detection_text_content.visibility = View.VISIBLE
        } catch (ex: Exception) {
            Log.d(classTag, "Failed to load picture", ex)
            Snackbar.make(face_detection_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT).show()
        }

        return photo
    }


    private fun getInfoFromFaces(faces: List<FirebaseVisionFace>) {
        for (face in faces) {
            val bounds = face.boundingBox
            val rotY = face.headEulerAngleY  // Head is rotated to the right rotY degrees
            val rotZ = face.headEulerAngleZ  // Head is tilted sideways rotZ degrees

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
            val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
            leftEar?.let {
                val leftEarPos = leftEar.position
                Log.e(classTag, "LeftEarPos: $leftEarPos")
            }

            // If contour detection was enabled:
            val leftEyeContour = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points
            val upperLipBottomContour = face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points

            // If classification was enabled:
            if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val smileProb = face.smilingProbability
                Log.e(classTag, "SmileProb: $smileProb")
            }
            if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val rightEyeOpenProb = face.rightEyeOpenProbability
                Log.e(classTag, "RightEyeOpenProb: $rightEyeOpenProb")
            }

            // If face tracking was enabled:
            if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
                val id = face.trackingId
                Log.e(classTag, "ID: $id")
            }

            Log.e(classTag, "Bounds: $bounds, RotY: $rotY, RotZ: $rotZ, LeftEar: $leftEar, " +
                    "LeftEyeContour: $leftEyeContour, UpperLipBottomContour: $upperLipBottomContour")
        }
    }

    private fun detectFaces(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)

        // Real-time contour detection of multiple faces
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build()

//        // High-accuracy landmark detection and face classification
//        val options = FirebaseVisionFaceDetectorOptions.Builder()
//            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
//            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
//            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
//            .build()

        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                getInfoFromFaces(faces)
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
            }
    }
}
