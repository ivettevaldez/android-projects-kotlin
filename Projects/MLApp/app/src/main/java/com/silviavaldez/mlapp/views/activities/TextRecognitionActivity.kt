package com.silviavaldez.mlapp.views.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import kotlinx.android.synthetic.main.activity_text_recognition.*

class TextRecognitionActivity : AppCompatActivity() {

    private val classTag: String? = TextRecognitionActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        cameraHelper.setCamera(recognition_fab)
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
                        Snackbar.make(recognition_layout, R.string.error_opening_camera, Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(classTag, "Permission denied :(")
                    Snackbar.make(
                        recognition_layout,
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
                    lockViews(true)

                    val photo = cameraHelper.showBitmap(recognition_image_photo)
                    if (photo != null) {
                        readText(photo)
                    } else {
                        showProgress(false)
                        Snackbar.make(recognition_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        when {
            show -> {
                recognition_progress.visibility = View.VISIBLE
                recognition_fab.isEnabled = false
            }
            else -> {
                recognition_progress.visibility = View.GONE
                recognition_fab.isEnabled = true
            }
        }
    }

    private fun lockViews(lock: Boolean) {
        if (lock) {
            showProgress(true)

            recognition_text_title.visibility = View.GONE
            recognition_text_content.visibility = View.GONE
            recognition_text_instruction.visibility = View.GONE
        } else {
            showProgress(false)

            recognition_text_title.visibility = View.VISIBLE
            recognition_text_content.visibility = View.VISIBLE
        }
    }

    private fun getInfoFromText(result: FirebaseVisionText) {
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox

            Log.i(classTag, "BlockText: $blockText, BlockCornerPoints: $blockCornerPoints, BlockFrame: $blockFrame")

            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox

                Log.i(classTag, "LineText: $lineText, LineCornerPoints: $lineCornerPoints, LineFrame: $lineFrame")

                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox

                    Log.i(
                        classTag,
                        "ElementText: $elementText, ElementCornerPoints: $elementCornerPoints, ElementFrame: $elementFrame"
                    )
                }
            }
        }
    }

    private fun readText(photo: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(photo)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                recognition_text_content.text = firebaseVisionText.text
                lockViews(false)
                getInfoFromText(firebaseVisionText)
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
                showProgress(false)
            }
    }
}
