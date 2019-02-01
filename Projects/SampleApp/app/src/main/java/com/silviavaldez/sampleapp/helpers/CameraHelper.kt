package com.silviavaldez.sampleapp.helpers

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.widget.ImageView
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.FilesHelper.Companion.imageFilePath
import java.io.IOException

const val CAMERA_REQUEST_CODE = 1

class CameraHelper(private val activity: Activity) {

    private val classTag = CameraHelper::class.simpleName

    private fun startCameraIntent(): Boolean {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(activity.packageManager) != null) {
                val imageUri = FilesHelper(activity).createImageUri()

                if (imageUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    activity.startActivityForResult(
                            intent,
                            CAMERA_REQUEST_CODE
                    )
                    return true
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
                    Log.e(classTag,
                            "Attempting to call camera without explicit permission", ex)
                }
                else -> {
                    Log.e(classTag, "Attempting to call camera intent", ex)
                }
            }
        }
        return false
    }

    fun openCamera(grantResults: IntArray): Int {
        // If request is cancelled, the result arrays are empty.
        if (PermissionHelper(activity).validatePermissionResult(grantResults)) {
            Log.e(classTag, "Permission granted! :D")

            val startedIntent = startCameraIntent()
            if (!startedIntent) {
                return R.string.error_opening_camera
            }
        } else {
            Log.e(classTag, "Permission denied :(")
            return R.string.error_missing_permissions
        }
        return 0
    }

    fun setCamera(fab: FloatingActionButton) {
        fab.setOnClickListener {
            if (PermissionHelper(activity).requestAllPermissions()) {
                startCameraIntent()
            }
        }
    }

    fun showBitmap(imageView: ImageView): Bitmap? {
        var photo: Bitmap? = null

        try {
            photo = BitmapFactory.decodeFile(imageFilePath)
            imageView.setImageBitmap(photo)
        } catch (ex: Exception) {
            Log.d(classTag, "Failed to load picture", ex)
        }

        return photo
    }
}