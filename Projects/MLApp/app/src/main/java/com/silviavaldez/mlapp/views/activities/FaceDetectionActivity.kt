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
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import com.silviavaldez.mlapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_face_detection.*

class FaceDetectionActivity : AppCompatActivity() {

    private val classTag: String? = FaceDetectionActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    private var utilHelper: UtilHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)

        cameraHelper.setCamera(face_detection_fab)
        utilHelper = UtilHelper(
            face_detection_progress,
            face_detection_fab,
            face_detection_text_instruction,
            null,
            face_detection_text_content
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
                    Snackbar.make(face_detection_layout, result, Snackbar.LENGTH_SHORT).show()
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

                    val photo = cameraHelper.showBitmap(face_detection_image_photo)
                    if (photo != null) {
                        Snackbar.make(face_detection_layout, R.string.msg_please_wait, Snackbar.LENGTH_LONG)
                            .show()

                        detectFaces(photo)
                    } else {
                        utilHelper?.showProgress(false)
                        Snackbar.make(face_detection_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getDataFromFaces(faces: List<FirebaseVisionFace>) {
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
            val leftEyeContour =
                face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points
            val upperLipBottomContour =
                face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points

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

            Log.e(
                classTag, "Bounds: $bounds, RotY: $rotY, RotZ: $rotZ, LeftEar: $leftEar, " +
                        "LeftEyeContour: $leftEyeContour, UpperLipBottomContour: $upperLipBottomContour"
            )
        }
    }

    private fun recognizeMood(faces: List<FirebaseVisionFace>): String {
        var mood: Int = R.string.face_detection_anything

        for (face in faces) {
            if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val smileProb = face.smilingProbability

                mood = if (smileProb > 0.9) {
                    R.string.face_detection_very_happy
                } else if (smileProb > 0.7 && smileProb <= 0.9) {
                    R.string.face_detection_happy
                } else if (smileProb > 0.4 && smileProb <= 0.7) {
                    R.string.face_detection_not_so_happy
                } else {
                    R.string.face_detection_so_sad
                }
            }
        }

        return getString(mood)
    }

    private fun detectFaces(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)

//        // Real-time contour detection of multiple faces
//        val options = FirebaseVisionFaceDetectorOptions.Builder()
//            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
//            .enableTracking()
//            .build()

        // High-accuracy landmark detection and face classification
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                utilHelper?.lockViews(false)

                val mood = recognizeMood(faces)
                face_detection_text_content.text = mood

                getDataFromFaces(faces)
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
                utilHelper?.showProgress(false)
            }
    }
}
