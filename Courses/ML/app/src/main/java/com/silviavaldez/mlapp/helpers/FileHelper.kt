package com.silviavaldez.mlapp.helpers

import android.app.Activity
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_PREFIX = "JPEG"
private const val FILENAME_SUFFIX = ".jpg"
private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmSS"

class FileHelper(private val activity: Activity) {

    private val classTag = FileHelper::class.simpleName

    companion object {
        var imageFilePath: String? = null
    }

    fun createImageUri(): Uri? {
        var imageUri: Uri? = null
        val imageFile = createImageFile()

        if (imageFile != null) {
            imageUri = FileProvider
                .getUriForFile(
                    activity.applicationContext,
                    "${activity.packageName}.fileprovider",
                    imageFile
                )
        } else {
            Log.e(classTag, "Can't create uri: file is NULL")
        }

        return imageUri
    }

    private fun validateDirectory(): File? {
        val directory: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (directory != null && !directory.exists()) {
            val created = directory.mkdirs()

            if (!created) {
                Log.e(classTag, "Can't create files directory")
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
}