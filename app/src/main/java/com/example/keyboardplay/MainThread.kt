package com.example.keyboardplay

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class MainThread(val surfaceHolder: SurfaceHolder, val keyboardView: KeyboardView) : Thread() {
    var running = false
    private var canvas: Canvas? = null

    override fun run() {
        while (running) {
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    keyboardView.update()
                    keyboardView.draw(canvas)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}