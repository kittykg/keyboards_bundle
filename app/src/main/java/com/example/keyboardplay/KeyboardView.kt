package com.example.keyboardplay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button

class KeyboardView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                println(v.toString())
                pressCount++
                println(pressCount)
            }
            MotionEvent.ACTION_UP -> {
                pressCount--
                println(pressCount)
            }
        }
        findViewById<Button>(R.id.button).setText(pressCount.toString())
        return true
    }

    var thread = MainThread(holder, this)
    var pressCount = 0

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry = true
        while (retry) {
            try {
                thread.running = false
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread.running = true
        thread.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            canvas.drawColor(Color.WHITE)
            val paint = Paint()
            paint.color = Color.rgb(0, 255, 255)
            canvas.drawRect(10.toFloat(), 10.toFloat(), 200.toFloat(), 200.toFloat(), paint)
        }
    }


    fun update() {}
}