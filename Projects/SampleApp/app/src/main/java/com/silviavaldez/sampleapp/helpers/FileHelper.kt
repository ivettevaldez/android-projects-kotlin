package com.silviavaldez.sampleapp.helpers

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val PROJECT_DIRECTORY = "SilviaValdez"
private const val APP_NAME = "SampleApp"

private const val FILENAME_PREFIX = "JPEG"
private const val FILENAME_SUFFIX = ".jpg"
private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmSS"

class FilesHelper(val context: Context) {

    private val tag: String = FilesHelper::class.java.simpleName
    private val directoryName = "$PROJECT_DIRECTORY${File.separator}$APP_NAME"

    companion object {
        var imageFilePath: String? = null
    }

    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }

    private fun getDirectory(): File {
        lateinit var directory: File

        directory = if (isExternalStorageWritable()) {
            // Find the root of the external storage
            val rootStorage = android.os.Environment.getExternalStorageDirectory()
            File(String.format("%s%s%s", rootStorage, File.separator, directoryName))
        } else {
            File(context.filesDir.toString())
        }

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            val created = directory.mkdirs()
            Log.e(tag, "Created directory: $created")
        }

        return directory
    }

    fun createFile(fileName: String) {
        try {
            val file = getFile(fileName)
            file.createNewFile()
        } catch (ex: FileNotFoundException) {
            Log.e(tag, "Attempting to find file: $fileName", ex)
        } catch (ex: IOException) {
            Log.e(tag, "Attempting to create file: $fileName", ex)
        }
    }

    fun getFile(fileName: String): File {
        return File(getDirectory(), fileName)
    }

    fun write(fileName: String, content: String) {
        try {
            val file = getFile(fileName)
            val writer = FileWriter(file)
            writer.append(content)
            writer.flush()
            writer.close()
        } catch (ex: FileNotFoundException) {
            Log.e(tag, "Attempting to find file: $fileName", ex)
        } catch (ex: IOException) {
            Log.e(tag, "Attempting to write to file: $fileName", ex)
        }
    }

    fun deleteFile(fileName: String) {
        try {
            getFile(fileName).delete()
        } catch (ex: Exception) {
            Log.e(tag, "Attempting to delete file: $fileName", ex)
        }
    }

    fun createImageUri(): Uri? {
        var imageUri: Uri? = null
        val imageFile = createImageFile()

        if (imageFile != null) {
            imageUri = FileProvider.getUriForFile(context,
                    "${context.packageName}.fileprovider",
                    imageFile)
        } else {
            Log.e(tag, "Can't create uri: file is NULL")
        }

        return imageUri
    }

    private fun getPicturesDirectory(): File? {
        val directory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (directory != null && !directory.exists()) {
            val created = directory.mkdirs()

            if (!created) {
                Log.e(tag, "Can't create files directory")
                return null
            }
        }

        Log.d(tag, "Directory: $directory")
        return directory
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(TIMESTAMP_FORMAT,
                Locale.getDefault()).format(Date())
        val fileName = "${FILENAME_PREFIX}_$timeStamp"
        val directory = getPicturesDirectory()
        val file = File.createTempFile(fileName, FILENAME_SUFFIX, directory)
        Log.d(tag, "File name: ${file.name}")

        // Save a reference to the absolute path
        imageFilePath = file.absolutePath

        return file
    }
}