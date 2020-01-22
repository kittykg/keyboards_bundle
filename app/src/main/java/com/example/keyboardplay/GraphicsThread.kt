package com.example.keyboardplay

import android.graphics.Canvas
import android.view.SurfaceHolder

class GraphicsThread(var surfaceHolder: SurfaceHolder, var renderer: MyRenderer) : Thread() {
    var running: Boolean = false
    override fun run() {
        while (running) {
            val canvas = this.surfaceHolder.lockCanvas()
            try {
                synchronized(surfaceHolder) {
                    this.renderer.update()
                    this.renderer.draw(canvas)
                }
            } catch (e: Exception) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}