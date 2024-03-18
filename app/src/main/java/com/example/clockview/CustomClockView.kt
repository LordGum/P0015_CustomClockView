package com.example.clockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.time.LocalDateTime
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
    }
    private var radius = 0f
    private val rect = Rect()

    private var mainColor = DEFAULT_MAIN_COLOR
    private var centerColor = DEFAULT_CENTER_COLOR


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
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            setBackgroundColor(centerColor)
            drawClockRound()
            drawNumbers()
            drawDots()

            drawArrows()
        }
        postInvalidateDelayed(1000)
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

    private fun Canvas.drawNumbers() {
        paintClock.style = Paint.Style.FILL
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


    private val paintArrows = Paint().apply {
        style = Paint.Style.FILL
        color = mainColor
    }
    private val paintSecArrow = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private fun Canvas.drawArrow(time: Double, sizeL: Float) {
        val angle = Math.PI * time/30 - Math.PI/2

        val leftPoint = ArrowData(
            radius + (cos(angle - Math.PI/2)).toFloat() * radius * 0.03f,
            radius + (sin(angle - Math.PI/2)).toFloat() * radius * 0.03f
        )
        val rightPoint = ArrowData(
            radius + (cos(angle + Math.PI/2)).toFloat() * radius * 0.03f,
            radius + (sin(angle + Math.PI/2)).toFloat() * radius * 0.03f
        )
        val topPoint = ArrowData(
            radius + cos(angle).toFloat() * radius * sizeL,
            radius + sin(angle).toFloat() * radius * sizeL
        )
        val bottomPoint = ArrowData(
            radius + (cos(angle + Math.PI)).toFloat() * radius * 0.03f,
            radius + (sin(angle + Math.PI)).toFloat() * radius * 0.03f
        )

        val path = Path()
        path.reset()

        with(path) {
            moveTo(rightPoint.x, rightPoint.y)
            lineTo(bottomPoint.x, bottomPoint.y)
            lineTo(leftPoint.x, leftPoint.y)
            lineTo(topPoint.x, topPoint.y)
        }

        this.drawPath(path, paintArrows)
    }

    private fun Canvas.drawSecArrow(time: Double) {
        val angle = Math.PI * time/30 - Math.PI/2
        paintSecArrow.strokeWidth = 5f

        drawLine(
            radius,
            radius,
            radius + cos(angle).toFloat() * radius * 0.8f,
            radius + sin(angle).toFloat() * radius * 0.8f,
            paintSecArrow
        )
    }

    private fun Canvas.drawArrows() = with(LocalDateTime.now()) {
        drawArrow(
            time = (hour % 12 + minute / 60f) * 5.0,
            sizeL = 0.4f
        )

        drawArrow(
            time = minute.toDouble(),
            sizeL = 0.6f
        )

        drawSecArrow(
            time = second.toDouble()
        )
    }
}

private data class ArrowData(
    val x: Float,
    val y: Float
)