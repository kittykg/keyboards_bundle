package com.example.keyboardplay

import android.content.Context.SENSOR_SERVICE
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorManager
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.roundToInt
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.widget.LinearLayout


class MyRenderer(private val context: FutureKeyboard) : SurfaceView(context), SurfaceHolder.Callback {
    private lateinit var thread: GraphicsThread
    private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    private val ballLogic = BallLogic(width.toFloat(), height.toFloat())
    private lateinit var holes: List<KeyHole>
    private var holeRadius = 70.0f
    private val ballRadius = holeRadius * 0.9f
    private val holePaint = Paint()
    private val ballPaint = Paint()
    private val textPaint = Paint()
    private var maxHeight = height / 2 - holeRadius
    private var maxWidth = width / 2 - holeRadius
    private var keyboardMode = Mode.LOWER_CASE

    init {

        ballPaint.color = Color.RED

        holePaint.color = Color.BLACK

        textPaint.color = Color.MAGENTA
        textPaint.textSize = holeRadius

        isFocusable = true
        holder.addCallback(this)

        makeHoles()

    }

    private fun makeHoles() {
        val keyHoleValues = when (keyboardMode) {
            Mode.LOWER_CASE -> ('a'..'z')
            Mode.UPPER_CASE -> ('A'..'Z')
            Mode.NUMBER_PUNCT -> ('!'..'?')
        }.map { KeyHoleValue.Letter(it) }
            .union(
                listOf(
                    KeyHoleValue.ChangeMode(),
                    KeyHoleValue.Backspace(),
                    KeyHoleValue.Enter(),
                    KeyHoleValue.Space()
                )
            )
        holes = keyHoleValues.mapIndexed { i, keyHoleValue ->

            val angle = (i.toFloat() / keyHoleValues.size) * 2 * Math.PI
            val x = maxWidth * Math.sin(angle) + width / 2
            val y = maxHeight * Math.cos(angle) + height / 2

            KeyHole(keyHoleValue, x.toFloat(), y.toFloat())
        }


    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        ballLogic.keyboardWidth = width.toFloat()
        ballLogic.keyboardHeight = height.toFloat()
        ballLogic.reset()

        maxHeight = height / 2 - holeRadius
        maxWidth = width / 2 - holeRadius

        makeHoles()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        sensorManager.registerListener(ballLogic, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)

        thread = GraphicsThread(holder, this)
        thread.running = true
        thread.start()
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.running = false
        thread.join()

        sensorManager.unregisterListener(ballLogic)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        //draw circle

        if (canvas != null) {
            canvas.drawColor(Color.CYAN)
            drawHoles(canvas)
            drawBall(canvas)
            drawRecentText(canvas)
        }
    }

    private fun drawRecentText(canvas: Canvas) {
        val recentText = context.getRecentText()

        val textShape = Rect()
        textPaint.getTextBounds(recentText, 0, recentText.length, textShape)
        val xTextOffset = textShape.width() / 2
        val yTextOffset = textShape.height() / 2

        canvas.drawText(recentText, width.toFloat() / 2 - xTextOffset, height.toFloat() / 2 + yTextOffset, textPaint)
    }

    private fun drawHoles(canvas: Canvas) {
        holes.forEach { it.draw(canvas) }
    }

    private fun drawBall(canvas: Canvas) {
        canvas.drawCircle(ballLogic.bx, ballLogic.by, ballRadius, ballPaint)
    }

    fun update() {
        ballLogic.update()
        detectCollision()
    }

    private fun detectCollision() {
        val x = (ballLogic.by - height / 2.0f) / maxHeight
        val y = (ballLogic.bx - width / 2.0f) / maxWidth

        var angle = Math.atan2(y.toDouble(), x.toDouble())

        if (angle < 0) {
            angle += 2 * Math.PI
        }

        val index = (angle * holes.size / (2 * Math.PI)).roundToInt() % holes.size
        val candidateHole = holes[index]
        val xDiff = Math.pow((ballLogic.bx - candidateHole.x).toDouble(), 2.0)
        val yDiff = Math.pow((ballLogic.by - candidateHole.y).toDouble(), 2.0)

        if (Math.sqrt(xDiff + yDiff) <= holeRadius) {
            pressKey(candidateHole.keyHoleValue)
            ballLogic.reset()
        }
    }

    private fun pressKey(keyHoleValue: KeyHoleValue) {
        when (keyHoleValue) {
            is KeyHoleValue.Letter ->
                context.typeLetter(keyHoleValue.char)
            is KeyHoleValue.ChangeMode -> {
                if (keyboardMode < Mode.NUMBER_PUNCT) {
                    keyboardMode = Mode.values().get(keyboardMode.ordinal + 1)
                } else {
                    keyboardMode = Mode.LOWER_CASE
                }
                makeHoles()
            }
            is KeyHoleValue.Backspace ->
                context.backspace()
            is KeyHoleValue.Enter ->
                context.typeLetter('\n')
            is KeyHoleValue.Space ->
                context.typeLetter(' ')
        }
    }

    inner class KeyHole(val keyHoleValue: KeyHoleValue, val x: Float, val y: Float) {
        fun draw(canvas: Canvas) {
            canvas.drawCircle(x, y, holeRadius, holePaint)

            val textShape = Rect()
            textPaint.getTextBounds(keyHoleValue.string, 0, keyHoleValue.string.length, textShape)
            val xTextOffset = textShape.width() / 2
            val yTextOffset = textShape.height() / 2

            canvas.drawText(keyHoleValue.string, x - xTextOffset, y + yTextOffset, textPaint)
        }
    }
}