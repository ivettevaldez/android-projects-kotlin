package com.silviavaldez.mlapp.helpers

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.widget.ImageView
import java.io.IOException

const val CAMERA_REQUEST_CODE = 1

class CameraHelper(private val activity: Activity) {

    private val classTag = CameraHelper::class.simpleName

    fun setCamera(fab: FloatingActionButton) {
        fab.setOnClickListener {
            if (PermissionHelper(activity).checkAllPermissions()) {
                startCameraIntent()
            }
        }
    }

    fun startCameraIntent(): Boolean {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(activity.packageManager) != null) {
                val imageUri = FileHelper(activity).createImageUri()

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
                    Log.e(classTag, "Attempting to call camera without explicit permission", ex)
                }
                else -> {
                    Log.e(classTag, "Attempting to call camera intent", ex)
                }
            }
        }
        return false
    }

    fun showBitmap(imageView: ImageView): Bitmap? {
        var photo: Bitmap? = null

        try {
            photo = BitmapFactory.decodeFile(FileHelper.imageFilePath)
            imageView.setImageBitmap(photo)
        } catch (ex: Exception) {
            Log.d(classTag, "Failed to load picture", ex)
        }

        return photo
    }
}