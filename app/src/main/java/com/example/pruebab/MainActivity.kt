package com.example.pruebab

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var compassView: CompassView? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val r = FloatArray(9)
    private val i = FloatArray(9)

    private var azimuth = 0f
    private var sensorData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compassView = CompassView(this)
        setContentView(compassView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(
            mySensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager!!.registerListener(
            mySensorEventListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(mySensorEventListener)
    }

    /**
     * Based on: https://developer.android.com/reference/kotlin/android/hardware/SensorEvent
     */
    private val mySensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) { // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            synchronized(this) {
                Log.i("GAME", Calendar.getInstance().timeInMillis.toString())
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    mGravity[0] = ALPHA * mGravity[0] + (1 - ALPHA) * event.values[0]
                    mGravity[1] = ALPHA * mGravity[1] + (1 - ALPHA) * event.values[1]
                    mGravity[2] = ALPHA * mGravity[2] + (1 - ALPHA) * event.values[2]
                }
                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    mGeomagnetic[0] = ALPHA * mGeomagnetic[0] + (1 - ALPHA) * event.values[0]
                    mGeomagnetic[1] = ALPHA * mGeomagnetic[1] + (1 - ALPHA) * event.values[1]
                    mGeomagnetic[2] = ALPHA * mGeomagnetic[2] + (1 - ALPHA) * event.values[2]
                }
                val success =
                    SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic)
                if (success) {
                    sensorData = true
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    azimuth = (azimuth + 360) % 360
                    compassView?.setDirection(azimuth)
                }
            }
        }
    }

    companion object {
        private const val ALPHA = 0.9f
    }

}