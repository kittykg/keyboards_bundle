package com.example.keyboardplay

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class BallLogic(var keyboardWidth: Float, var keyboardHeight: Float) : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val acceleration = 2.0f
    private val maxVelocity = 20.0f
    private val resistance = 0.95f

    private var bddx = 0.0f
    private var bdx = 0.0f
    var bx: Float = 0.0f
    private var bddy = 0.0f
    private var bdy = 0.0f
    var by = 0.0f

    private fun updateBallPosition() {
        bx += bdx
        by += bdy

        val newBx = toRange(bx, 0.0f, keyboardWidth)
        val newBy = toRange(by, 0.0f, keyboardHeight)

        if (bx != newBx) {
            bdx = 0.0f
            bx = newBx
        }

        if (by != newBy) {
            bdy = 0.0f
            by = newBy
        }

    }

    override fun onSensorChanged(event: SensorEvent) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
            val pitch = event.values[1]
            val roll = event.values[2]

            bddx = tiltToAcceleration(roll)
            bddy = tiltToAcceleration(pitch)
        }
    }

    private fun tiltToAcceleration(tilt: Float) : Float {
        return Math.sin(Math.toRadians(-tilt.toDouble())).toFloat() * acceleration
    }

    private fun toRange(value: Float, min: Float, max: Float): Float {
        return Math.min(Math.max(value, min), max)
    }

    private fun updateBallVelocity() {
        bdx *= resistance
        bdy *= resistance

        bdx += bddx
        bdy += bddy

        bdx = toRange(bdx, -maxVelocity, maxVelocity)
        bdy = toRange(bdy, -maxVelocity, maxVelocity)
    }

    fun update() {
        updateBallVelocity()
        updateBallPosition()
    }

    fun reset() {
        bddx = 0.0f
        bdx = 0.0f
        bx = keyboardWidth / 2.0f

        bddy = 0.0f
        bdy = 0.0f
        by = keyboardHeight / 2.0f
    }
}