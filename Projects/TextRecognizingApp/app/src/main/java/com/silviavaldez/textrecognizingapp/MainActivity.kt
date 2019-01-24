package com.silviavaldez.textrecognizingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.silviavaldez.textrecognizingapp.helpers.FileHelper
import com.silviavaldez.textrecognizingapp.helpers.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

const val CAMERA_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

    private val classTag = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        setCamera()
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
                    startCameraIntent()
                } else {
                    Log.e(classTag, "Permission denied :(")
                    Snackbar.make(main_layout, R.string.error_missing_permissions, Snackbar.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Hide instruction
                    main_text_instruction.visibility = View.GONE

                    val photo = showBitmap()
                    if (photo != null) {
                        readText(photo)
                    }
                }
            }
        }
    }

    private fun showBitmap(): Bitmap? {
        var photo: Bitmap? = null

        try {
            photo = BitmapFactory.decodeFile(FileHelper.imageFilePath)
            main_image_photo.setImageBitmap(photo)

            // Show text
            main_text_title.visibility = View.VISIBLE
            main_text_content.visibility = View.VISIBLE
        } catch (ex: Exception) {
            Log.d(classTag, "Failed to load picture", ex)
            Snackbar.make(main_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT).show()
        }

        return photo
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
                main_text_content.text = firebaseVisionText.text
                getInfoFromText(firebaseVisionText)
            }
            .addOnFailureListener {
                Log.e(classTag, "Failed to process image", it)
            }
    }

    private fun startCameraIntent() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(packageManager) != null) {
                val imageUri = FileHelper(this).createImageUri()

                if (imageUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                    return
                } else {
                    Log.e(classTag, "Can't call intent: uri is NULL")
                }
            } else {
                Log.e(classTag, "Can't resolve activity")
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> {
                    Log.e(classTag, "Attempting to create image file", ex)
                }
                is SecurityException -> {
                    Log.e(classTag, "Attempting to call camera without explicit permission", ex)
                }
                else -> {
                    Log.e(classTag, "Attempting to call camera intent", ex)
                }
            }
        }

        Snackbar.make(main_layout, R.string.error_opening_camera, Snackbar.LENGTH_LONG).show()
    }

    private fun setCamera() {
        main_fab?.setOnClickListener {
            if (PermissionHelper(this).checkAllPermissions()) {
                main_text_content.text = ""
                startCameraIntent()
            }
        }
    }
}
