package com.example.clockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CustomClockView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {

    companion object {
        private const val STROKE_WIDTH = 15f

        private const val DEFAULT_MAIN_COLOR = Color.BLACK
        private const val DEFAULT_CENTER_COLOR = Color.WHITE
        private const val DEFAULT_CONTAINER_COLOR = Color.GRAY
    }
    private var radius = 0f
    private val rect = Rect()

    private var mainColor = DEFAULT_MAIN_COLOR
    private var centerColor = DEFAULT_CENTER_COLOR
    private var containerColor = DEFAULT_CONTAINER_COLOR


    private val paintClock = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        color = mainColor
    }

        init {
        setupAttrs(attrs)
    }

    private fun setupAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomClockView)

        with(typedArray) {
            mainColor = getColor(R.styleable.CustomClockView_mainColor, DEFAULT_MAIN_COLOR)
            paintClock.color = mainColor

            centerColor = getColor(R.styleable.CustomClockView_centerColor, DEFAULT_CENTER_COLOR)
            containerColor = getColor(R.styleable.CustomClockView_containerColor, DEFAULT_CONTAINER_COLOR)
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            setBackgroundColor(centerColor)
            drawClockRound()
            drawCenter()
            drawNumbers()
            drawDots()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = Integer.min(width, height)

        radius = size / 2f

        setMeasuredDimension(size, size)
    }

    private fun Canvas.drawClockRound() {
        paintClock.style = Paint.Style.STROKE
        drawCircle(
            radius,
            radius,
            radius * 0.9f,
            paintClock
        )
    }

    private fun Canvas.drawCenter() {
        paintClock.style = Paint.Style.FILL
        drawCircle(
            radius,
            radius,
            10f,
            paintClock
        )
    }

    private fun Canvas.drawNumbers() {
        paintClock.textSize = radius / 100 * 20f
        for (num in 1..12) {
            val number = num.toString()
            paintClock.getTextBounds(number, 0, number.length, rect)
            val angle = Math.PI / 6 * (num - 3)
            val x = radius - rect.width() / 2 + 0.73 * cos(angle) * radius
            val y = radius + rect.height() / 2 + 0.73 * sin(angle) * radius
            drawText(number, x.toFloat(), y.toFloat(), paintClock)
        }
    }

    private fun Canvas.drawDots() {
        for (dot in 1..60) {
            val dotText = "."
            paintClock.getTextBounds(dotText, 0, dotText.length, rect)
            val angle = Math.PI / 30 * (dot)
            val x = radius - rect.width() / 2 +  0.85 * cos(angle) * radius
            val y = radius + rect.height() / 2 +  0.85 * sin(angle) * radius
            drawText(dotText, x.toFloat(), y.toFloat(), paintClock)
        }
    }

}