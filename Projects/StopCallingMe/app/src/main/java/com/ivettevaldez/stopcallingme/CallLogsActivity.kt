package com.ivettevaldez.stopcallingme

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Long
import java.util.*

class CallLogsActivity : AppCompatActivity() {

    private val classTag: String = this::class.java.simpleName
    private val permissionCodeReadPhoneState: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_logs)

        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionCodeReadPhoneState -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Permission granted: $permissionCodeReadPhoneState",
                        Toast.LENGTH_SHORT
                    ).show()

                    getCallLogs()
                } else {
                    Toast.makeText(
                        this,
                        "Permission NOT granted: $permissionCodeReadPhoneState",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG
            )
            requestPermissions(permissions, permissionCodeReadPhoneState)
        } else {
            getCallLogs()
        }
    }

    private fun getCallLogs() {
        val projection = arrayOf(
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        try {
            val contacts: Uri = CallLog.Calls.CONTENT_URI
            val cursor: Cursor? = applicationContext.contentResolver.query(
                contacts,
                projection,
                null,
                null,
                CallLog.Calls.DATE + " DESC;"
            )

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val indexName: Int = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                    val indexNumber: Int = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                    val indexType: Int = cursor.getColumnIndex(CallLog.Calls.TYPE)
                    val indexDate: Int = cursor.getColumnIndex(CallLog.Calls.DATE)
                    val indexDuration: Int = cursor.getColumnIndex(CallLog.Calls.DURATION)

                    val name: String? = cursor.getString(indexName)
                    val number: String? = cursor.getString(indexNumber)
                    val type: String? = cursor.getString(indexType)
                    val date: String? = cursor.getString(indexDate)
                    val duration: Int = cursor.getInt(indexDuration)

                    val dayTime = if (date != null) {
                        Date(Long.valueOf(date)).toString()
                    } else {
                        null
                    }

                    Log.e(
                        classTag,
                        "NAME: $name, NUMBER: $number, TYPE: $type, DURATION: $duration, DATE: $dayTime"
                    )
                }

                cursor.close()
            } else {
                Log.e(classTag, "Null cursor")
            }
        } catch (ex: Exception) {
            Log.e(classTag, ex.message, ex)
        }
    }
}