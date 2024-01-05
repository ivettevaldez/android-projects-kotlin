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
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import com.silviavaldez.mlapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_landmark_recognition.*
import kotlinx.android.synthetic.main.activity_text_recognition.*

class TextRecognitionActivity : AppCompatActivity() {

    private val classTag: String? = TextRecognitionActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    private var utilHelper: UtilHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        cameraHelper.setCamera(recognition_fab)
        utilHelper = UtilHelper(
            recognition_progress,
            recognition_fab,
            recognition_text_instruction,
            recognition_text_title,
            recognition_text_content
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
                    Snackbar.make(recognition_layout, result, Snackbar.LENGTH_SHORT).show()
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

                    val photo = cameraHelper.showBitmap(recognition_image_photo)
                    if (photo != null) {
                        readText(photo)
                    } else {
                        utilHelper?.showProgress(false)
                        Snackbar.make(recognition_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getDataFromText(result: FirebaseVisionText) {
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox

            Log.i(
                classTag, "BlockText: $blockText, BlockCornerPoints: $blockCornerPoints, " +
                        "BlockFrame: $blockFrame"
            )

            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox

                Log.i(
                    classTag, "LineText: $lineText, LineCornerPoints: $lineCornerPoints, " +
                            "LineFrame: $lineFrame"
                )

                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox

                    Log.i(
                        classTag,
                        "ElementText: $elementText, ElementCornerPoints: $elementCornerPoints, " +
                                "ElementFrame: $elementFrame"
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
                utilHelper?.lockViews(false)
                getDataFromText(firebaseVisionText)
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
                Snackbar.make(landmark_layout, R.string.error_processing_picture, Snackbar.LENGTH_LONG).show()
                utilHelper?.showProgress(false)
            }
    }
}
