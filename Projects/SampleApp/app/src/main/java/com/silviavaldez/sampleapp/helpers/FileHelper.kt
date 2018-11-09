package com.silviavaldez.sampleapp.helpers

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException

private const val PROJECT_DIRECTORY = "SilviaValdez"
private const val APP_NAME = "SampleApp"

class FilesHelper(val context: Context) {

    private val tag: String = FilesHelper::class.java.simpleName
    private val directoryName = "$PROJECT_DIRECTORY${File.separator}$APP_NAME"

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
}