package com.silviavaldez.textrecognizingapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const val CAMERA_REQUEST_CODE = 1

private const val FILENAME_PREFIX = "JPEG"
private const val FILENAME_SUFFIX = ".jpg"
private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmSS"

class MainActivity : AppCompatActivity() {

    private val classTag = MainActivity::class.simpleName
    private var imageFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                    showBitmap()
                }
            }
        }
    }

    private fun showBitmap() {
        try {
            val photo = BitmapFactory.decodeFile(imageFilePath)
            main_image_photo.setImageBitmap(photo)

            // Show text
            main_text_title.visibility = View.VISIBLE
            main_text_content.visibility = View.VISIBLE
        } catch (ex: Exception) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show()
            Log.d(classTag, "Failed to load", ex)
        }
    }

    private fun validateDirectory(): File? {
        val directory: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (directory != null && !directory.exists()) {
            val created = directory.mkdirs()

            if (!created) {
                Log.e(classTag, "Can't create files directory")
                Snackbar.make(main_layout, R.string.error_opening_camera, Snackbar.LENGTH_LONG).show()
                return null
            }
        }

        Log.d(classTag, "Directory: $directory")
        return directory
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).format(Date())
        val fileName = "${FILENAME_PREFIX}_$timeStamp"
        val directory = validateDirectory()
        val file = File.createTempFile(fileName, FILENAME_SUFFIX, directory)
        Log.d(classTag, "File name: ${file.name}")

        // Save a reference to the absolute path
        imageFilePath = file.absolutePath

        return file
    }

    private fun createImageUri(): Uri? {
        var imageUri: Uri? = null
        val imageFile = createImageFile()

        if (imageFile != null) {
            imageUri = FileProvider.getUriForFile(
                applicationContext,
                "$packageName.fileprovider",
                imageFile
            )
        } else {
            Log.e(classTag, "Can't create uri: file is NULL")
        }

        return imageUri
    }

    private fun startCameraIntent() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(packageManager) != null) {
                val imageUri = createImageUri()

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
                startCameraIntent()
            }
        }
    }
}
