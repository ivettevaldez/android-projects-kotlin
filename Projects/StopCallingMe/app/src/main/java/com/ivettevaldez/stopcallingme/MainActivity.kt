package com.ivettevaldez.stopcallingme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val permissionCodeReadPhoneState: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
        setListenerEvents()
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
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            )
            requestPermissions(permissions, permissionCodeReadPhoneState)
        }
    }

    private fun setListenerEvents() {
        val button = findViewById<Button>(R.id.main_button_call_logs)
        button.setOnClickListener {
            goToCallLogsScreen()
        }
    }

    private fun goToCallLogsScreen() {
        val intent = Intent(this, CallLogsActivity::class.java)
        startActivity(intent)
    }
}