package com.silviavaldez.sampleapp.views.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Window
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import kotlinx.android.synthetic.main.activity_accelerometer.*

class AccelerometerActivity : BaseActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerometer)

        setUpTypefaces()
        getSensor()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(event: Sensor?, accuracy: Int) {
        // Nothing to do here.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            acceleromter_text_x_value.text = String.format("%.2f", event.values[0])
            acceleromter_text_y_value.text = String.format("%.2f", event.values[1])
            acceleromter_text_z_value.text = String.format("%.2f", event.values[2])
        }
    }

    private fun getSensor() {
        // Get reference of the sensor service
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Focus in accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun setUpTypefaces() {
        val typefaceHelper = TypefaceHelper(this)
        typefaceHelper.overrideAllTypefaces()
        typefaceHelper.setUpActionBar(title.toString(), true)
    }
}
